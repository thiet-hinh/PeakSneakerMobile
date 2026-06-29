package com.example.shoestore.service;

import com.example.shoestore.dto.request.CheckoutPreviewRequest;
import com.example.shoestore.dto.response.CheckoutCartItem;
import com.example.shoestore.dto.response.CheckoutPreviewResponse;
import com.example.shoestore.dto.response.ShippingAddressCheckout;
import com.example.shoestore.entity.*;
import com.example.shoestore.repository.AddressRepository;
import com.example.shoestore.repository.CartItemRepository;
import com.example.shoestore.repository.ImageRepository;
import com.example.shoestore.thirdparty.cloudinary.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartService cartService;
    private final ProductVariantService productVariantService;
    private final AddressRepository addressRepository;
    private final ImageRepository imageRepository;
    private final CloudinaryService cloudinaryService;

    public List<CartItem> findByCartId(Integer cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    public List<CartItem> findByUserId(Integer userId) {
        Cart cart = cartService.findByUserId(userId);
        return cartItemRepository.findByCartId(cart.getId());
    }

    @Transactional
    public CartItem addToCart(Integer userId, Integer variantId, Integer quantity) {
        Cart cart = cartService.findByUserId(userId);
        ProductVariant variant = productVariantService.findById(variantId);

        return cartItemRepository.findByCartIdAndProductVariantId(cart.getId(), variantId)
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + quantity);
                    return cartItemRepository.save(existing);
                })
                .orElseGet(() -> cartItemRepository.save(CartItem.builder()
                        .cart(cart).productVariant(variant).quantity(quantity).build()));
    }

    @Transactional
    public CartItem updateQuantity(Integer cartItemId, Integer quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found: " + cartItemId));
        if (quantity <= 0) {
            cartItemRepository.delete(item);
            return null;
        }
        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    @Transactional
    public void removeItem(Integer cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Transactional
    public void clearCart(Integer userId) {
        Cart cart = cartService.findByUserId(userId);
        cartItemRepository.deleteByCartId(cart.getId());
    }

    @Transactional(readOnly = true)
    public CheckoutPreviewResponse getCheckoutPreview(Integer userId, CheckoutPreviewRequest request) {
        Cart cart = cartService.findByUserId(userId);
        List<CartItem> cartItems = cartItemRepository.findByIdIn(request.getCartItemIds()).stream()
                .filter(item -> item.getCart().getId().equals(cart.getId()))
                .toList();

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Không có sản phẩm hợp lệ để thanh toán.");
        }

        List<CheckoutCartItem> items = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            ProductVariant variant = cartItem.getProductVariant();
            Product product = variant.getProduct();

            if (variant.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException(product.getName() + " không đủ số lượng trong kho.");
            }

            String image = imageRepository.findFirstByProductVariantIdAndIsPrimaryTrue(variant.getId())
                    .map(Image::getImageName)
                    .orElseGet(() -> imageRepository.findFirstByProductIdAndIsPrimaryTrue(product.getId())
                            .map(Image::getImageName).orElse(""));

            CheckoutCartItem dto = new CheckoutCartItem();
            dto.setCartItemId(cartItem.getId());
            dto.setVariantId(variant.getId());
            dto.setImageUrl(cloudinaryService.createImageUrl(image));
            dto.setBrand(product.getBrand().getName());
            dto.setProductName(product.getName());
            dto.setColor(variant.getColor());
            dto.setSize(variant.getSize());
            dto.setQuantity(cartItem.getQuantity());
            dto.setPrice(product.getPrice().doubleValue());
            items.add(dto);
        }

        ShippingAddressCheckout shippingDTO = null;
        Optional<Address> optionalAddress = addressRepository.findByUserIdAndIsDefaultTrue(userId);
        if (optionalAddress.isPresent()) {
            Address address = optionalAddress.get();
            shippingDTO = new ShippingAddressCheckout();
            shippingDTO.setId(address.getId());
            shippingDTO.setReceiverName(address.getUserName());
            shippingDTO.setPhone(address.getPhone());
            shippingDTO.setAddress(address.getFullAddress());
        }

        CheckoutPreviewResponse response = new CheckoutPreviewResponse();
        response.setShippingAddress(shippingDTO);
        response.setItems(items);
        return response;
    }
}