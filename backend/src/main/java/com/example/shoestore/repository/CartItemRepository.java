package com.example.shoestore.repository;

import com.example.shoestore.entity.Cart;
import com.example.shoestore.entity.CartItem;
import com.example.shoestore.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    List<CartItem> findByCartId(Integer cartId);

    Optional<CartItem> findByCartAndProductVariant(Cart cart, ProductVariant productVariant);

    Optional<CartItem> findByCartIdAndProductVariantId(Integer cartId, Integer productVariantId);

    List<CartItem> findByIdIn(List<Integer> ids);

    void deleteByCartId(Integer cartId);

    void deleteByCartIdAndProductVariantId(Integer cartId, Integer productVariantId);
}