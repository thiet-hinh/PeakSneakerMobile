package com.example.shoestore.service;

import com.example.shoestore.entity.Review;
import com.example.shoestore.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;


    public Review findById(Integer id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
    }

    public List<Review> findByProductId(Integer productId) {
        return reviewRepository.findByProductId(productId);
    }

    public Review findByUserIdAndProductId(Integer userId, Integer productId) {
        return reviewRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new RuntimeException(
                        "Review not found with userId: " + userId + " and productId: " + productId));
    }

    public boolean existsByUserIdAndProductId(Integer userId, Integer productId) {
        return reviewRepository.existsByUserIdAndProductId(userId, productId);
    }

    public Double findAverageStarByProductId(Integer productId) {
        Double avg = reviewRepository.findAverageStarByProductId(productId);
        return avg != null ? avg : 0.0;
    }

    public long countByProductId(Integer productId) {
        return reviewRepository.countByProductId(productId);
    }

    @Transactional
    public Review save(Review review) {

        Integer userId = review.getUser().getId();
        Integer productId = review.getProduct().getId();
        if (reviewRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new RuntimeException(
                    "Review already exists with userId: " + userId + " and productId: " + productId);
        }

        if (review.getStar() != null && (review.getStar() < 1 || review.getStar() > 5)) {
            throw new RuntimeException("Star must be between 1 and 5");
        }
        return reviewRepository.save(review);
    }

    @Transactional
    public Review update(Integer id, Review updated) {

        Review existing = findById(id);

        if (updated.getStar() != null && (updated.getStar() < 1 || updated.getStar() > 5)) {
            throw new RuntimeException("Star must be between 1 and 5");
        }

        existing.setComment(updated.getComment());
        existing.setStar(updated.getStar());

        return reviewRepository.save(existing);
    }

    @Transactional
    public void deleteById(Integer id) {

        if (!reviewRepository.existsById(id)) {
            throw new RuntimeException("Review not found with id: " + id);
        }

        reviewRepository.deleteById(id);
    }
}
