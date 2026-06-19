package com.example.shoestore.service;

import com.example.shoestore.entity.Payment;
import com.example.shoestore.enums.PaymentStatus;
import com.example.shoestore.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment findById(Integer id) {
        return paymentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found with id: " + id));
    }

    public Payment findByOrderId(Integer orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found for order id: " + orderId));
    }

    public List<Payment> findByStatus(PaymentStatus status) {
        return paymentRepository.findByPaymentStatus(status);
    }

    public boolean existsByOrderId(Integer orderId) {
        return paymentRepository.existsByOrderId(orderId);
    }

    @Transactional
    public Payment save(Payment payment) {

        Integer orderId = payment.getOrder().getId();

        if (paymentRepository.existsByOrderId(orderId)) {
            throw new RuntimeException(
                    "Payment already exists for order id: " + orderId);
        }

        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment update(Integer id, Payment updated) {

        Payment existing = findById(id);

        existing.setAmount(updated.getAmount());
        existing.setPaidAt(updated.getPaidAt());
        existing.setPaymentMethod(updated.getPaymentMethod());
        existing.setPaymentStatus(updated.getPaymentStatus());
        existing.setTransactionId(updated.getTransactionId());

        return paymentRepository.save(existing);
    }

    @Transactional
    public Payment updateStatus(Integer paymentId, PaymentStatus status) {

        Payment payment = findById(paymentId);
        payment.setPaymentStatus(status);
        if (status == PaymentStatus.PAID) {
            payment.setPaidAt(LocalDateTime.now());
        }
        return paymentRepository.save(payment);
    }

}
