package com.example.shoestore.repository;

import com.example.shoestore.entity.Order;
import com.example.shoestore.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUserId(Integer userId);

    List<Order> findByUserIdAndStatus(Integer userId, OrderStatus status);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);

    long countByStatus(OrderStatus status);
}
