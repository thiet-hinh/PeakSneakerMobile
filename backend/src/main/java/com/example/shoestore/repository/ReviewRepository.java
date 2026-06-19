package com.example.shoestore.repository;

import com.example.shoestore.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByProductId(Integer productId);

    Optional<Review> findByUserIdAndProductId(Integer userId, Integer productId);

    boolean existsByUserIdAndProductId(Integer userId, Integer productId);

    @Query("SELECT AVG(r.star) FROM Review r WHERE r.product.id = :productId")
    Double findAverageStarByProductId(@Param("productId") Integer productId);

    long countByProductId(Integer productId);
}
