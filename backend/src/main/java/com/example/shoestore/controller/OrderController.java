package com.example.shoestore.controller;

import com.example.shoestore.dto.response.OrderDetailResponse;
import com.example.shoestore.dto.response.OrderResponse;
import com.example.shoestore.enums.OrderStatus;
import com.example.shoestore.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrdersService ordersService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserAndStatus(@PathVariable Integer userId, @RequestParam(required = false) String status) {
        return ResponseEntity.ok(ordersService.getOrderResponsesByUserAndStatus(userId, status));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(@PathVariable Integer orderId) {
        return ResponseEntity.ok(ordersService.getOrderDetail(orderId));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderDetailResponse> updateOrderStatus(@PathVariable Integer orderId, @RequestParam OrderStatus status) {
        ordersService.updateStatus(orderId, status);
        return ResponseEntity.ok(ordersService.getOrderDetail(orderId));
    }
}
