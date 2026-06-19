package com.example.shoestore.service;

import com.example.shoestore.entity.Cart;
import com.example.shoestore.entity.CartItem;
import com.example.shoestore.entity.ProductVariant;
import com.example.shoestore.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartService cartService;
    private final ProductVariantService productVariantService;

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

        return cartItemRepository
                .findByCartIdAndProductVariantId(cart.getId(), variantId)
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + quantity);
                    return cartItemRepository.save(existing);
                })
                .orElseGet(() -> cartItemRepository.save(
                        CartItem.builder().cart(cart)
                                .productVariant(variant)
                                .quantity(quantity)
                                .build()
                ));
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
}