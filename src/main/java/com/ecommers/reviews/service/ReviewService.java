package com.ecommers.reviews.service;

import com.ecommers.reviews.dto.ReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {
    ReviewDto.ReviewResponse createReview(ReviewDto.ReviewRequest request);
    Page<ReviewDto.ReviewResponse> getAllReviews(Pageable pageable);
    ReviewDto.ReviewResponse getReviewById(Long id);
    List<ReviewDto.ReviewResponse> getReviewsByProductId(Long productId);
    List<ReviewDto.ReviewResponse> getReviewsByUserId(Long userId);
    ReviewDto.ReviewResponse updateReview(Long id, ReviewDto.ReviewRequest request);
    ReviewDto.ReviewResponse patchReview(Long id, ReviewDto.ReviewPatchRequest request);
    void deleteReview(Long id);
}
