package com.ecommers.reviews.controller;

import com.ecommers.reviews.dto.ReviewDto;
import com.ecommers.reviews.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Reseñas", description = "Reseñas de productos; valida usuario y producto contra sus microservicios (Feign)")
public class ReviewController {

    private final ReviewService service;

    @PostMapping
    @Operation(summary = "Crear una reseña",
            description = "Valida que el usuario y el producto existan antes de guardar.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reseña creada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (rating fuera de 1-5, comentario vacío)"),
            @ApiResponse(responseCode = "404", description = "El usuario o el producto no existen")
    })
    public ResponseEntity<ReviewDto.ReviewResponse> addReview(@Valid @RequestBody ReviewDto.ReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createReview(request));
    }

    @GetMapping
    @Operation(summary = "Listar reseñas paginadas")
    @ApiResponse(responseCode = "200", description = "Página de reseñas")
    public ResponseEntity<Page<ReviewDto.ReviewResponse>> getAll(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(service.getAllReviews(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una reseña por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña encontrada"),
            @ApiResponse(responseCode = "404", description = "La reseña no existe")
    })
    public ResponseEntity<ReviewDto.ReviewResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getReviewById(id));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Listar las reseñas de un producto")
    @ApiResponse(responseCode = "200", description = "Reseñas del producto (puede estar vacía)")
    public ResponseEntity<List<ReviewDto.ReviewResponse>> getByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(service.getReviewsByProductId(productId));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Listar las reseñas de un usuario")
    @ApiResponse(responseCode = "200", description = "Reseñas del usuario (puede estar vacía)")
    public ResponseEntity<List<ReviewDto.ReviewResponse>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getReviewsByUserId(userId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Reemplazar una reseña completa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña actualizada"),
            @ApiResponse(responseCode = "404", description = "La reseña no existe")
    })
    public ResponseEntity<ReviewDto.ReviewResponse> update(@PathVariable Long id, @Valid @RequestBody ReviewDto.ReviewRequest request) {
        return ResponseEntity.ok(service.updateReview(id, request));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente una reseña (rating y/o comentario)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña actualizada"),
            @ApiResponse(responseCode = "404", description = "La reseña no existe")
    })
    public ResponseEntity<ReviewDto.ReviewResponse> patch(@PathVariable Long id, @Valid @RequestBody ReviewDto.ReviewPatchRequest request) {
        return ResponseEntity.ok(service.patchReview(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una reseña")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reseña eliminada"),
            @ApiResponse(responseCode = "404", description = "La reseña no existe")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
