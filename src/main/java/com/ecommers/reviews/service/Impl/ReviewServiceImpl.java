package com.ecommers.reviews.service.Impl;

import com.ecommers.reviews.client.ProductClient;
import com.ecommers.reviews.client.UserClient;
import com.ecommers.reviews.dto.ReviewDto;
import com.ecommers.reviews.exception.ProductNotFoundException;
import com.ecommers.reviews.exception.ReviewNotFoundException;
import com.ecommers.reviews.exception.UserNotFoundException;
import com.ecommers.reviews.model.Review;
import com.ecommers.reviews.repository.ReviewRepository;
import com.ecommers.reviews.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;
    private final ProductClient productClient;
    private final UserClient userClient;

    @Override
    public ReviewDto.ReviewResponse createReview(ReviewDto.ReviewRequest request) {
        try {
            productClient.getProductById(request.productId());
        } catch (Exception e) {
            throw new ProductNotFoundException("El producto con ID " + request.productId() + " no existe");
        }

        try {
            userClient.getUserById(request.userId());
        } catch (Exception e) {
            throw new UserNotFoundException("El usuario con ID " + request.userId() + " no existe");
        }

        Review review = new Review();
        review.setProductId(request.productId());
        review.setUserId(request.userId());
        review.setRating(request.rating());
        review.setComment(request.comment());

        Review saved = repository.save(review);
        return toResponse(saved);
    }

    @Override
    public List<ReviewDto.ReviewResponse> getAllReviews() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public ReviewDto.ReviewResponse getReviewById(Long id) {
        return toResponse(repository.findById(id).orElseThrow(() -> new ReviewNotFoundException(id)));
    }

    @Override
    public List<ReviewDto.ReviewResponse> getReviewsByProductId(Long productId) {
        return repository.findByProductId(productId).stream().map(this::toResponse).toList();
    }

    @Override
    public List<ReviewDto.ReviewResponse> getReviewsByUserId(Long userId) {
        return repository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    @Override
    public ReviewDto.ReviewResponse updateReview(Long id, ReviewDto.ReviewRequest request) {
        Review review = repository.findById(id).orElseThrow(() -> new ReviewNotFoundException(id));

        try {
            productClient.getProductById(request.productId());
        } catch (Exception e) {
            throw new ProductNotFoundException("El producto con ID " + request.productId() + " no existe");
        }

        try {
            userClient.getUserById(request.userId());
        } catch (Exception e) {
            throw new UserNotFoundException("El usuario con ID " + request.userId() + " no existe");
        }

        review.setProductId(request.productId());
        review.setUserId(request.userId());
        review.setRating(request.rating());
        review.setComment(request.comment());

        return toResponse(repository.save(review));
    }

    @Override
    public ReviewDto.ReviewResponse patchReview(Long id, ReviewDto.ReviewPatchRequest request) {
        Review review = repository.findById(id).orElseThrow(() -> new ReviewNotFoundException(id));

        if (request.rating() != null) review.setRating(request.rating());
        if (request.comment() != null) review.setComment(request.comment());

        return toResponse(repository.save(review));
    }

    @Override
    public void deleteReview(Long id) {
        if (!repository.existsById(id)) throw new ReviewNotFoundException(id);
        repository.deleteById(id);
    }

    private ReviewDto.ReviewResponse toResponse(Review r) {
        return new ReviewDto.ReviewResponse(r.getId(), r.getProductId(), r.getUserId(), r.getRating(), r.getComment());
    }
}
