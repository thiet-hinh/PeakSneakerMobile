package com.example.shoestore.service;

import com.example.shoestore.dto.response.OrderDetailResponse;
import com.example.shoestore.dto.response.OrderItemDetailResponse;
import com.example.shoestore.dto.response.OrderResponse;
import com.example.shoestore.entity.*;
import com.example.shoestore.enums.OrderStatus;
import com.example.shoestore.repository.ImageRepository;
import com.example.shoestore.repository.OrderRepository;
import com.example.shoestore.thirdparty.cloudinary.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
}
