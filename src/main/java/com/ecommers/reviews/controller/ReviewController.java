package com.ecommers.reviews.controller;

import com.ecommers.reviews.dto.ReviewDto;
import com.ecommers.reviews.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService service;

    @PostMapping
    public ResponseEntity<EntityModel<ReviewDto.ReviewResponse>> addReview(@Valid @RequestBody ReviewDto.ReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(service.createReview(request)));
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ReviewDto.ReviewResponse>>> getAll(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<ReviewDto.ReviewResponse> page = service.getAllReviews(pageable);
        List<EntityModel<ReviewDto.ReviewResponse>> content = page.getContent().stream()
                .map(this::toModel)
                .toList();
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
        return ResponseEntity.ok(PagedModel.of(content, metadata,
                linkTo(methodOn(ReviewController.class).getAll(pageable)).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ReviewDto.ReviewResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(toModel(service.getReviewById(id)));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<CollectionModel<EntityModel<ReviewDto.ReviewResponse>>> getByProduct(@PathVariable Long productId) {
        List<EntityModel<ReviewDto.ReviewResponse>> reviews = service.getReviewsByProductId(productId).stream()
                .map(this::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(reviews,
                linkTo(methodOn(ReviewController.class).getByProduct(productId)).withSelfRel()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CollectionModel<EntityModel<ReviewDto.ReviewResponse>>> getByUser(@PathVariable Long userId) {
        List<EntityModel<ReviewDto.ReviewResponse>> reviews = service.getReviewsByUserId(userId).stream()
                .map(this::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(reviews,
                linkTo(methodOn(ReviewController.class).getByUser(userId)).withSelfRel()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ReviewDto.ReviewResponse>> update(@PathVariable Long id, @Valid @RequestBody ReviewDto.ReviewRequest request) {
        return ResponseEntity.ok(toModel(service.updateReview(id, request)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<ReviewDto.ReviewResponse>> patch(@PathVariable Long id, @Valid @RequestBody ReviewDto.ReviewPatchRequest request) {
        return ResponseEntity.ok(toModel(service.patchReview(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<ReviewDto.ReviewResponse> toModel(ReviewDto.ReviewResponse review) {
        return EntityModel.of(review,
                linkTo(methodOn(ReviewController.class).getById(review.id())).withSelfRel(),
                linkTo(methodOn(ReviewController.class).getByProduct(review.productId())).withRel("product-reviews"),
                linkTo(methodOn(ReviewController.class).getByUser(review.userId())).withRel("user-reviews"),
                linkTo(methodOn(ReviewController.class).getAll(null)).withRel("reviews"));
    }
}
