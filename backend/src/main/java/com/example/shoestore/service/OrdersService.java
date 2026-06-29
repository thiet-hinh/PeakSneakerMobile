package com.example.shoestore.service;

import com.example.shoestore.dto.request.PlaceOrderRequest;
import com.example.shoestore.dto.response.*;
import com.example.shoestore.entity.*;
import com.example.shoestore.enums.DeliveryMethod;
import com.example.shoestore.enums.OrderStatus;
import com.example.shoestore.enums.PaymentMethod;
import com.example.shoestore.enums.PaymentStatus;
import com.example.shoestore.repository.ImageRepository;
import com.example.shoestore.repository.OrderItemRepository;
import com.example.shoestore.repository.OrderRepository;
import com.example.shoestore.thirdparty.cloudinary.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrderRepository orderRepository;
    private final ProductVariantService productVariantService;
    private final CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final OrderItemRepository orderItemRepository;

    private final UserService userService;
    private final CartItemService cartItemService;
    private final AddressService addressService;
    private final VoucherService voucherService;
    private final UserVoucherService userVoucherService;
    private final PaymentService paymentService;

    public List<OrderResponse> getOrderResponsesByUserAndStatus(Integer userId, String status) {
        List<Order> orders;
        if (status == null || status.isEmpty()) {
            orders = orderRepository.findByUserIdOrderByOrderDateDesc(userId);
        } else {
            OrderStatus backendStatus = OrderStatus.valueOf(status);
            orders = orderRepository.findByUserIdAndStatusOrderByOrderDateDesc(userId, backendStatus);
        }
        return orders.stream().map(this::convertToOrderResponse).collect(Collectors.toList());
    }

    private OrderResponse convertToOrderResponse(Order order) {
        return OrderResponse.builder()
                .orderCode(String.valueOf(order.getId()))
                .orderDate(order.getOrderDate().format(DATE_FORMATTER))
                .price(order.getFinalAmount())
                .status(order.getStatus().name())
                .build();
    }

    public Order findById(Integer id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @Transactional
    public Order placeOrder(Order order) {
        order.setStatus(OrderStatus.PROCESSING);
        order.setOrderDate(LocalDateTime.now());
        order.getOrderItems().forEach(item -> {
            if (item.getProductVariant() != null) {
                productVariantService.decreaseStock(item.getProductVariant().getId(), item.getQuantity());
            }
        });
        return orderRepository.save(order);
    }

    public OrderDetailResponse getOrderDetail(Integer orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderItemDetailResponse> items = order.getOrderItems().stream().map(item -> {
            ProductVariant variant = item.getProductVariant();
            Product product = variant.getProduct();

            String image = imageRepository.findFirstByProductIdAndIsPrimaryTrue(product.getId())
                    .map(img -> cloudinaryService.createImageUrl(img.getImageName()))
                    .orElse(null);

            return OrderItemDetailResponse.builder()
                    .productName(item.getProductName())
                    .brand(product.getBrand().getName())
                    .color(variant.getColor())
                    .size(variant.getSize())
                    .quantity(item.getQuantity())
                    .price(item.getUnitPrice())
                    .imageUrl(image)
                    .build();
        }).toList();

        String fullAddress = order.getShippingStreet() + ", " + order.getShippingWard() + ", " + order.getShippingDistrict() + ", " + order.getShippingProvince();

        return OrderDetailResponse.builder()
                .id(order.getId())
                .subtotal(order.getSubtotalAmount())
                .shippingFee(order.getShippingFee())
                .discountAmount(order.getDiscountAmount())
                .finalAmount(order.getFinalAmount())
                .shippingName(order.getShippingName())
                .shippingPhone(order.getShippingPhone())
                .shippingAddress(fullAddress)
                .status(order.getStatus().name())
                .orderDate(order.getOrderDate())
                .items(items)
                .build();
    }

    @Transactional
    public Order updateStatus(Integer orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        if (status == OrderStatus.CANCELLED) {
            if (order.getStatus() != OrderStatus.PROCESSING) {
                throw new RuntimeException("Đơn hàng không thể hủy");
            }
            // Trả lại tồn kho
            for (OrderItem item : order.getOrderItems()) {
                ProductVariant variant = item.getProductVariant();
                if (variant != null) {
                    productVariantService.increaseStock(variant.getId(), item.getQuantity());
                }
            }
        }

        order.setStatus(status);
        return orderRepository.save(order);
    }


    @Transactional
    public PlaceOrderResponse placeOrder(PlaceOrderRequest request) {
        User user = userService.findById(request.getUserId());

        List<CartItem> cartItems = cartItemService.findByUserId(request.getUserId()).stream()
                .filter(item -> request.getSelectedCartItemIds().contains(item.getId())).toList();

        if (cartItems.size() != request.getSelectedCartItemIds().size()) {
            throw new RuntimeException("Sản phẩm trong giỏ hàng không hợp lệ hoặc đã bị thay đổi.");
        }

        AddressResponse address = addressService.findDefaultByUserId(request.getUserId());
        BigDecimal subtotal = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            ProductVariant variant = cartItem.getProductVariant();
            if (variant.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Sản phẩm " + variant.getProduct().getName() + " không đủ số lượng.");
            }
            BigDecimal unitPrice = variant.getProduct().getPrice();
            subtotal = subtotal.add(unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        Voucher voucher = null;
        BigDecimal discountAmount = BigDecimal.ZERO;

        if (request.getVoucherCode() != null && !request.getVoucherCode().isBlank()) {
            voucher = voucherService.findByCode(request.getVoucherCode());
            discountAmount = voucher.getDiscountValue();
        }

        BigDecimal shippingFee = request.getDeliveryMethod() == DeliveryMethod.FAST ? BigDecimal.valueOf(35000) : BigDecimal.ZERO;

        Order order = createOrder(request, user, address, voucher, subtotal, shippingFee, discountAmount);
        order = orderRepository.save(order);

        createOrderItems(order, cartItems);

        if (voucher != null) {
            applyVoucher(user, voucher, order);
        }

        createPayment(order, request);
        removeCartItems(cartItems);

        return PlaceOrderResponse.builder()
                .orderCode(String.valueOf(order.getId()))
                .estimatedDeliveryDate(order.getExpectedDate().toLocalDate())
                .paymentMethod(request.getPaymentMethod().name())
                .build();
    }

    private Order createOrder(PlaceOrderRequest request, User user, AddressResponse address, Voucher voucher, BigDecimal subtotal, BigDecimal shippingFee, BigDecimal discountAmount) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expectedDate = now.plusDays(shippingFee.compareTo(BigDecimal.ZERO) == 0 ? 7 : 3);
        BigDecimal finalAmount = subtotal.add(shippingFee).subtract(discountAmount);

        return Order.builder()
                .user(user)
                .subtotalAmount(subtotal)
                .discountAmount(discountAmount)
                .shippingFee(shippingFee)
                .finalAmount(finalAmount)
                .shippingName(address.getUserName())
                .shippingPhone(address.getPhone())
                .shippingProvince(address.getProvinceName())
                .shippingDistrict(address.getDistrictName())
                .shippingWard(address.getWardName())
                .shippingStreet(address.getStreetDetail())
                .status(OrderStatus.PROCESSING)
                .voucherId(voucher == null ? null : voucher.getId())
                .orderDate(now)
                .expectedDate(expectedDate)
                .build();
    }

    private void createOrderItems(Order order, List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            ProductVariant variant = cartItem.getProductVariant();
            Product product = variant.getProduct();
            BigDecimal unitPrice = product.getPrice();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .productVariant(variant)
                    .productName(product.getName())
                    .variantName("Color:" + variant.getColor() + ", Size:" + variant.getSize())
                    .quantity(cartItem.getQuantity())
                    .unitPrice(unitPrice)
                    .subtotal(subtotal)
                    .build();

            orderItemRepository.save(orderItem);
            productVariantService.decreaseStock(variant.getId(), cartItem.getQuantity());
        }
    }

    private void applyVoucher(User user, Voucher voucher, Order order) {
        UserVoucher userVoucher;
        try {
            userVoucher = userVoucherService.findByUserIdAndVoucherId(user.getId(), voucher.getId());
            if (Boolean.TRUE.equals(userVoucher.getIsUsed())) throw new RuntimeException("Voucher đã được sử dụng.");
        } catch (RuntimeException e) {
            userVoucher = userVoucherService.save(UserVoucher.builder().user(user).voucher(voucher).isUsed(false).build());
        }
        userVoucherService.markAsUsed(userVoucher.getId(), order);
        voucherService.incrementUsedCount(voucher.getId());
    }

    private void createPayment(Order order, PlaceOrderRequest request) {
        PaymentMethod paymentMethod = request.getPaymentMethod();
        if (paymentMethod == null) {
            throw new RuntimeException("Phương thức thanh toán không hợp lệ.");
        }

        boolean isVnPay = paymentMethod == PaymentMethod.VNPAY;
        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getFinalAmount())
                .paymentMethod(paymentMethod)
                .paymentStatus(isVnPay ? PaymentStatus.PAID : PaymentStatus.PENDING)
                .transactionId(null)
                .paidAt(isVnPay ? LocalDateTime.now() : null)
                .build();

        paymentService.save(payment);
    }

    private void removeCartItems(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            cartItemService.removeItem(cartItem.getId());
        }
    }
}
