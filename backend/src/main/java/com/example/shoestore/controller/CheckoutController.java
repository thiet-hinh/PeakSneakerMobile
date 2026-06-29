package com.example.shoestore.controller;

import com.example.shoestore.dto.request.CheckoutPreviewRequest;
import com.example.shoestore.dto.response.CheckoutPreviewResponse;
import com.example.shoestore.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CartItemService cartItemService;

    @PostMapping("/preview/{userId}")
    public ResponseEntity<CheckoutPreviewResponse> getCheckoutPreview(@PathVariable Integer userId, @RequestBody CheckoutPreviewRequest request) {
        return ResponseEntity.ok(cartItemService.getCheckoutPreview(userId, request));
    }
}
