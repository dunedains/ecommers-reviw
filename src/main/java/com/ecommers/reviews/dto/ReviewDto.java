package com.ecommers.reviews.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class ReviewDto {

    public record ReviewRequest(
            @NotNull(message = "El ID del producto es obligatorio")
            Long productId,

            @NotNull(message = "El ID del usuario es obligatorio")
            Long userId,

            @NotNull(message = "La calificación es obligatoria")
            @Min(value = 1, message = "La calificación mínima es 1")
            @Max(value = 5, message = "La calificación máxima es 5")
            Integer rating,

            @Size(max = 1000, message = "El comentario no puede superar los 1000 caracteres")
            String comment
    ) {}

    public record ReviewPatchRequest(
            @Min(value = 1, message = "La calificación mínima es 1")
            @Max(value = 5, message = "La calificación máxima es 5")
            Integer rating,

            @Size(max = 1000, message = "El comentario no puede superar los 1000 caracteres")
            String comment
    ) {}

    public record ReviewResponse(
            Long id,
            Long productId,
            Long userId,
            Integer rating,
            String comment
    ) {}

    public record ProductDto(
            Long id,
            String name,
            String description,
            BigDecimal price
    ) {}

    public record UserDto(
            Long id,
            String name,
            String email,
            String address
    ) {}
}
