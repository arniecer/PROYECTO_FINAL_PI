package com.grandspicy.service;

import com.grandspicy.model.Review;
import com.grandspicy.repository.ReviewRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> findByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    public List<Review> findByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    public List<Review> findRecent(int limit) {
        return reviewRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, limit));
    }

    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }
}
