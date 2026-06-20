package com.example.shoestore.controller;

import com.example.shoestore.entity.Review;
import com.example.shoestore.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // Lấy tất cả review theo sản phẩm
    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<List<Review>> getByProduct(
            @PathVariable Integer productId) {
        return ResponseEntity.ok(reviewService.findByProductId(productId));
    }

    // Lấy điểm trung bình và số lượng review của sản phẩm
    @GetMapping("/products/{productId}/reviews/summary")
    public ResponseEntity<Map<String, Object>> getSummary(@PathVariable Integer productId) {
        return ResponseEntity.ok(Map.of(
                "averageStar", reviewService.findAverageStarByProductId(productId),
                "totalReviews", reviewService.countByProductId(productId)
        ));
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<Review> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(reviewService.findById(id));
    }

    // Tạo review mới
    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<Review> create(
            @PathVariable Integer productId,
            @RequestBody Review review) {
        return ResponseEntity.ok(reviewService.save(review));
    }
}