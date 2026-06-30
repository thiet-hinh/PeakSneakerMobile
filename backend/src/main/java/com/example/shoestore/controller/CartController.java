package com.example.shoestore.controller;

import com.example.shoestore.dto.request.CartRequest;
import com.example.shoestore.dto.response.CartItemResponse;
import com.example.shoestore.service.CartItemService;
import com.example.shoestore.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartItemService cartItemService;
    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody CartRequest request) {
        try {
            cartService.addToCart(request);
            return ResponseEntity.ok("Thêm vào giỏ hàng thành công!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemResponse>> getCart(@PathVariable Integer userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    // Sửa số lượng 1 item trong giỏ. Nếu quantity <= 0 thì item sẽ bị xóa luôn (trả 204).
    @PatchMapping("/items/{itemId}")
    public ResponseEntity<?> updateQuantity(
            @PathVariable Integer itemId,
            @RequestParam Integer quantity) {
        try {
            CartItemResponse updated = cartItemService.updateQuantity(itemId, quantity);
            if (updated == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Integer itemId) {
        cartItemService.removeItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Integer userId) {
        cartItemService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}