package com.example.shoestore.controller;

import com.example.shoestore.entity.Payment;
import com.example.shoestore.enums.PaymentStatus;
import com.example.shoestore.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(paymentService.findById(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Payment> getByOrder(@PathVariable Integer orderId) {
        return ResponseEntity.ok(paymentService.findByOrderId(orderId));
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> getByStatus(@PathVariable PaymentStatus status) {
        return ResponseEntity.ok(paymentService.findByStatus(status));
    }

    // Tạo bản ghi thanh toán khi đặt hàng
    @PostMapping
    public ResponseEntity<Payment> create(@RequestBody Payment payment) {
        return ResponseEntity.ok(paymentService.save(payment));
    }

    // Callback từ cổng thanh toán xác nhận thành công
    @PatchMapping("/order/{orderId}/paid")
    public ResponseEntity<Payment> markAsPaid(
            @PathVariable Integer orderId,
            @RequestParam String transactionId) {
        return ResponseEntity.ok(paymentService.markAsPaid(orderId, transactionId));
    }

    // Callback thanh toán thất bại
    @PatchMapping("/order/{orderId}/failed")
    public ResponseEntity<Payment> markAsFailed(@PathVariable Integer orderId) {
        return ResponseEntity.ok(paymentService.markAsFailed(orderId));
    }


    // Admin chỉnh sửa thủ công
    @PutMapping("/{id}")
    public ResponseEntity<Payment> update(@PathVariable Integer id, @RequestBody Payment payment) {
        return ResponseEntity.ok(paymentService.update(id, payment));
    }
}
