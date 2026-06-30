package com.example.shoestore.service;


import com.example.shoestore.dto.request.CartRequest;
import com.example.shoestore.dto.response.CartItemResponse;
import com.example.shoestore.entity.*;
import com.example.shoestore.repository.*;
import com.example.shoestore.thirdparty.cloudinary.CloudinaryService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    public Cart findByUserId(Integer userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));
    }

    @Transactional
    public void addToCart(CartRequest request) {
        ProductVariant variant = productVariantRepository
                .findByProductIdAndColorAndSize(request.getProductId(), request.getColor(), request.getSize())
                .orElseThrow(() -> new RuntimeException("Biến thể sản phẩm này không tồn tại!"));

        if (variant.getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Số lượng sản phẩm trong kho không đủ!");
        }

        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseGet(() -> {
                    User user = userRepository.findById(request.getUserId())
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProductVariant(cart, variant);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + request.getQuantity();
            if (newQuantity > variant.getStockQuantity()) {
                throw new RuntimeException("Số lượng sản phẩm trong kho không đủ!");
            }
            item.setQuantity(newQuantity);
            cartItemRepository.save(item);
        } else {
            // Nếu chưa có: Tạo mới một dòng CartItem
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProductVariant(variant);
            newItem.setQuantity(request.getQuantity());
            cartItemRepository.save(newItem);
        }
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> getCartByUserId(Integer userId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        if (cart == null || cart.getCartItems() == null) {
            return new ArrayList<>();
        }

        return cart.getCartItems().stream().map(this::toResponse).toList();
    }

    public CartItemResponse toResponse(CartItem item) {
        var variant = item.getProductVariant();
        var product = variant.getProduct();

        String imageName = imageRepository
                .findFirstByProductVariantIdOrderByIsPrimaryDescIdAsc(variant.getId())
                .map(Image::getImageName)
                .orElseGet(() -> imageRepository
                        .findFirstByProductIdAndIsPrimaryTrue(product.getId())
                        .map(Image::getImageName)
                        .orElse(""));

        String imageUrl = imageName.isEmpty() ? "" : cloudinaryService.createImageUrl(imageName);

        return new CartItemResponse(
                item.getId(),
                product.getId(),
                variant.getId(),
                product.getName(),
                imageUrl,
                variant.getColor(),
                variant.getSize(),
                product.getPrice(),
                item.getQuantity(),
                variant.getStockQuantity()
        );
    }
}