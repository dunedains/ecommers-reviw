package com.ecommers.reviews.controller;

import com.ecommers.reviews.dto.ReviewDto;
import com.ecommers.reviews.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService service;

    @PostMapping
    public ResponseEntity<ReviewDto.ReviewResponse> addReview(@Valid @RequestBody ReviewDto.ReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createReview(request));
    }

    @GetMapping
    public ResponseEntity<List<ReviewDto.ReviewResponse>> getAll() {
        return ResponseEntity.ok(service.getAllReviews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto.ReviewResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getReviewById(id));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewDto.ReviewResponse>> getByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(service.getReviewsByProductId(productId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDto.ReviewResponse>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getReviewsByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto.ReviewResponse> update(@PathVariable Long id, @Valid @RequestBody ReviewDto.ReviewRequest request) {
        return ResponseEntity.ok(service.updateReview(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReviewDto.ReviewResponse> patch(@PathVariable Long id, @Valid @RequestBody ReviewDto.ReviewPatchRequest request) {
        return ResponseEntity.ok(service.patchReview(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
