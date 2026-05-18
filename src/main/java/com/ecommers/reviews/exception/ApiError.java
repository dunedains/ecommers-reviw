package com.ecommers.reviews.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ApiError(
        int status,
        String message,
        LocalDateTime timestamp,
        List<String> errors
) {
    public ApiError(int status, String message) {
        this(status, message, LocalDateTime.now(), List.of());
    }

    public ApiError(int status, String message, List<String> errors) {
        this(status, message, LocalDateTime.now(), errors);
    }
}
