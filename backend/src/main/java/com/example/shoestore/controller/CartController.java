package com.example.shoestore.controller;

import com.example.shoestore.entity.CartItem;
import com.example.shoestore.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartItemService cartItemService;

    @GetMapping("/user/{userId}/")
    public ResponseEntity<List<CartItem>> getCart(@PathVariable Integer userId) {
        return ResponseEntity.ok(cartItemService.findByUserId(userId));
    }

    @PostMapping("/user/{userId}//items")
    public ResponseEntity<CartItem> addItem(
            @PathVariable Integer userId,
            @RequestParam Integer variantId,
            @RequestParam(defaultValue = "1") Integer quantity) {
        return ResponseEntity.ok(cartItemService.addToCart(userId, variantId, quantity));
    }

    @PatchMapping("/user/{userId}//items/{itemId}")
    public ResponseEntity<CartItem> updateQuantity(
            @PathVariable Integer itemId,
            @RequestParam Integer quantity) {
        CartItem updated = cartItemService.updateQuantity(itemId, quantity);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{userId}//items/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Integer itemId) {
        cartItemService.removeItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{userId}/")
    public ResponseEntity<Void> clearCart(@PathVariable Integer userId) {
        cartItemService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}