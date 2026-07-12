package com.ecommers.reviews.service.impl;

import com.ecommers.reviews.client.ProductClient;
import com.ecommers.reviews.client.UserClient;
import com.ecommers.reviews.dto.ReviewDto.ReviewPatchRequest;
import com.ecommers.reviews.dto.ReviewDto.ReviewRequest;
import com.ecommers.reviews.dto.ReviewDto.ReviewResponse;
import com.ecommers.reviews.exception.ProductNotFoundException;
import com.ecommers.reviews.exception.ReviewNotFoundException;
import com.ecommers.reviews.exception.UserNotFoundException;
import com.ecommers.reviews.model.Review;
import com.ecommers.reviews.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de las reseñas.
 * Se mockean el repositorio y los clientes de productos y usuarios (Feign).
 */
@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository repository;
    @Mock
    private ProductClient productClient;
    @Mock
    private UserClient userClient;

    @InjectMocks
    private ReviewServiceImpl service;

    @Test
    @DisplayName("createReview: si producto y usuario existen, guarda la reseña")
    void createReview_productoYUsuarioExisten_guarda() {
        // Given: validaciones remotas OK (no lanzan)
        when(repository.save(any(Review.class))).thenAnswer(i -> {
            Review r = i.getArgument(0);
            r.setId(1L);
            return r;
        });

        // When
        ReviewResponse response = service.createReview(
                new ReviewRequest(10L, 2L, 5, "Excelente"));

        // Then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.rating()).isEqualTo(5);
        verify(repository).save(any(Review.class));
    }

    @Test
    @DisplayName("createReview: si el producto no existe, lanza ProductNotFoundException")
    void createReview_productoInexistente_lanzaExcepcion() {
        when(productClient.getProductById(10L)).thenThrow(new RuntimeException("404"));

        assertThatThrownBy(() -> service.createReview(new ReviewRequest(10L, 2L, 5, "x")))
                .isInstanceOf(ProductNotFoundException.class);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("createReview: si el usuario no existe, lanza UserNotFoundException")
    void createReview_usuarioInexistente_lanzaExcepcion() {
        // producto OK (no stub → null), usuario lanza
        when(userClient.getUserById(2L)).thenThrow(new RuntimeException("404"));

        assertThatThrownBy(() -> service.createReview(new ReviewRequest(10L, 2L, 5, "x")))
                .isInstanceOf(UserNotFoundException.class);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("getReviewById: si no existe, lanza ReviewNotFoundException")
    void getReviewById_inexistente_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getReviewById(99L))
                .isInstanceOf(ReviewNotFoundException.class);
    }

    @Test
    @DisplayName("deleteReview: si no existe, lanza excepción y no borra")
    void deleteReview_inexistente_lanzaExcepcion() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteReview(99L))
                .isInstanceOf(ReviewNotFoundException.class);
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("updateReview: actualiza una reseña existente validando producto y usuario")
    void updateReview_existente_actualiza() {
        Review review = new Review();
        review.setId(1L);
        review.setRating(3);
        when(repository.findById(1L)).thenReturn(Optional.of(review));
        when(repository.save(any(Review.class))).thenAnswer(i -> i.getArgument(0));

        ReviewResponse response = service.updateReview(1L, new ReviewRequest(10L, 2L, 5, "Mejor"));

        assertThat(response.rating()).isEqualTo(5);
        assertThat(response.comment()).isEqualTo("Mejor");
    }

    @Test
    @DisplayName("patchReview: aplica solo los campos provistos")
    void patchReview_actualizaParcial() {
        Review review = new Review();
        review.setId(1L);
        review.setRating(3);
        review.setComment("viejo");
        when(repository.findById(1L)).thenReturn(Optional.of(review));
        when(repository.save(any(Review.class))).thenAnswer(i -> i.getArgument(0));

        ReviewResponse response = service.patchReview(1L, new ReviewPatchRequest(4, null));

        assertThat(response.rating()).isEqualTo(4);
        assertThat(response.comment()).isEqualTo("viejo"); // no se tocó
    }

    @Test
    @DisplayName("getReviewsByProductId / getReviewsByUserId / getAllReviews: mapean listas")
    void getReviews_devuelveListas() {
        Review r = new Review();
        r.setId(1L);
        r.setProductId(10L);
        r.setUserId(2L);
        r.setRating(5);
        when(repository.findByProductId(10L)).thenReturn(List.of(r));
        when(repository.findByUserId(2L)).thenReturn(List.of(r));
        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(r)));

        assertThat(service.getReviewsByProductId(10L)).hasSize(1);
        assertThat(service.getReviewsByUserId(2L)).hasSize(1);
        assertThat(service.getAllReviews(PageRequest.of(0, 10)).getContent()).hasSize(1);
    }
}
