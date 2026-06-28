package com.example.shoestore.repository;

import com.example.shoestore.entity.Order;
import com.example.shoestore.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUserIdOrderByOrderDateDesc(Integer userId);

    List<Order> findByUserIdAndStatusOrderByOrderDateDesc(Integer userId, OrderStatus status);

    @Query("""
            SELECT o
            FROM Order o
            LEFT JOIN FETCH o.orderItems oi
            LEFT JOIN FETCH oi.productVariant pv
            LEFT JOIN FETCH pv.product p
            LEFT JOIN FETCH p.brand
            WHERE o.id = :id
            """)
    Optional<Order> findById(Integer id);
}
