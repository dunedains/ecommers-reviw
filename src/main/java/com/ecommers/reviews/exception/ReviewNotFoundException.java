package com.ecommers.reviews.exception;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(Long id) {
        super("La reseña con ID " + id + " no existe");
    }
}
