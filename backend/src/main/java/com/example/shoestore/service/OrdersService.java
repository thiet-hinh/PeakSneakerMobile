package com.example.shoestore.service;

import com.example.shoestore.entity.Order;
import com.example.shoestore.enums.OrderStatus;
import com.example.shoestore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrderRepository OrderRepository;
    private final ProductVariantService productVariantService;

    public Order findById(Integer id) {
        return OrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public List<Order> findByUserId(Integer userId) {
        return OrderRepository.findByUserId(userId);
    }

    public List<Order> findByStatus(OrderStatus status) {
        return OrderRepository.findByStatus(status);
    }

    public List<Order> findByUserIdAndStatus(Integer userId, OrderStatus status) {
        return OrderRepository.findByUserIdAndStatus(userId, status);
    }

    public List<Order> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return OrderRepository.findByOrderDateBetween(start, end);
    }

    public long countByStatus(OrderStatus status) {
        return OrderRepository.countByStatus(status);
    }

    @Transactional
    public Order placeOrder(Order order) {
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());

        // Trừ tồn kho cho từng item
        order.getOrderItems().forEach(item -> {
            if (item.getProductVariant() != null) {
                productVariantService.decreaseStock(
                        item.getProductVariant().getId(),
                        item.getQuantity()
                );
            }
        });

        return OrderRepository.save(order);
    }

    @Transactional
    public Order updateStatus(Integer id, OrderStatus status) {
        Order order = findById(id);

        // Hoàn trả tồn kho nếu huỷ đơn
        if (status == OrderStatus.CANCELLED
                && order.getStatus() != OrderStatus.CANCELLED) {
            order.getOrderItems().forEach(item -> {
                if (item.getProductVariant() != null) {
                    productVariantService.increaseStock(
                            item.getProductVariant().getId(),
                            item.getQuantity()
                    );
                }
            });
        }

        order.setStatus(status);
        return OrderRepository.save(order);
    }

    @Transactional
    public void deleteById(Integer id) {
        OrderRepository.deleteById(id);
    }
}
