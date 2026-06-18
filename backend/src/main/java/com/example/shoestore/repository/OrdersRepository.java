package com.example.shoestore.repository;

import com.example.shoestore.entity.Orders;
import com.example.shoestore.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {

    List<Orders> findByUserId(Integer userId);

    Page<Orders> findByUserId(Integer userId, Pageable pageable);

    List<Orders> findByUserIdAndStatus(Integer userId, OrderStatus status);

    Page<Orders> findByStatus(OrderStatus status, Pageable pageable);

    List<Orders> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);

    long countByStatus(OrderStatus status);
}
