package com.example.shoestore.service;

import com.example.shoestore.entity.OrderItem;
import com.example.shoestore.repository.OrderItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItem findById(Integer id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderItem not found with id: " + id));
    }

    public List<OrderItem> findByOrderId(Integer orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }


    @Transactional
    public OrderItem save(OrderItem orderItem) {

        if (orderItem.getQuantity() == null || orderItem.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        if (orderItem.getUnitPrice() == null
                || orderItem.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Unit price must be greater than or equal to 0");
        }

        if (orderItem.getSubtotal() == null) {
            orderItem.setSubtotal(
                    orderItem.getUnitPrice()
                            .multiply(BigDecimal.valueOf(orderItem.getQuantity()))
            );
        }

        return orderItemRepository.save(orderItem);
    }

    @Transactional
    public OrderItem update(Integer id, OrderItem updated) {

        OrderItem existing = findById(id);

        if (updated.getQuantity() == null || updated.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        if (updated.getUnitPrice() == null
                || updated.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Unit price must be greater than or equal to 0");
        }

        existing.setProductName(updated.getProductName());
        existing.setVariantName(updated.getVariantName());
        existing.setQuantity(updated.getQuantity());
        existing.setUnitPrice(updated.getUnitPrice());

        existing.setSubtotal(
                updated.getUnitPrice()
                        .multiply(BigDecimal.valueOf(updated.getQuantity()))
        );

        return orderItemRepository.save(existing);
    }
}
