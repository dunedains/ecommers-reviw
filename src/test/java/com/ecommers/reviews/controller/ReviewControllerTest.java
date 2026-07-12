package com.ecommers.reviews.controller;

import com.ecommers.reviews.dto.ReviewDto.ReviewResponse;
import com.ecommers.reviews.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReviewService service;

    @Test
    @DisplayName("POST /api/reviews válido -> 201")
    void create_devuelve201() throws Exception {
        when(service.createReview(any())).thenReturn(new ReviewResponse(1L, 10L, 2L, 5, "Bueno"));

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":10,\"userId\":2,\"rating\":5,\"comment\":\"Bueno\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    @DisplayName("POST /api/reviews con rating fuera de rango -> 400")
    void create_ratingInvalido_devuelve400() throws Exception {
        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":10,\"userId\":2,\"rating\":10,\"comment\":\"x\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/reviews -> 200")
    void getAll_devuelve200() throws Exception {
        when(service.getAllReviews(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(new ReviewResponse(1L, 10L, 2L, 5, "Bueno"))));

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/reviews/product/{productId} -> 200")
    void getByProduct_devuelve200() throws Exception {
        when(service.getReviewsByProductId(10L)).thenReturn(List.of(new ReviewResponse(1L, 10L, 2L, 5, "Bueno")));

        mockMvc.perform(get("/api/reviews/product/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(10));
    }

    @Test
    @DisplayName("GET /api/reviews/{id} -> 200")
    void getById_devuelve200() throws Exception {
        when(service.getReviewById(1L)).thenReturn(new ReviewResponse(1L, 10L, 2L, 5, "Bueno"));

        mockMvc.perform(get("/api/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("GET /api/reviews/user/{userId} -> 200")
    void getByUser_devuelve200() throws Exception {
        when(service.getReviewsByUserId(2L)).thenReturn(List.of(new ReviewResponse(1L, 10L, 2L, 5, "Bueno")));

        mockMvc.perform(get("/api/reviews/user/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(2));
    }

    @Test
    @DisplayName("PUT /api/reviews/{id} -> 200")
    void update_devuelve200() throws Exception {
        when(service.updateReview(eq(1L), any()))
                .thenReturn(new ReviewResponse(1L, 10L, 2L, 4, "Regular"));

        mockMvc.perform(put("/api/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":10,\"userId\":2,\"rating\":4,\"comment\":\"Regular\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(4));
    }

    @Test
    @DisplayName("PATCH /api/reviews/{id} solo rating -> 200")
    void patch_devuelve200() throws Exception {
        when(service.patchReview(eq(1L), any()))
                .thenReturn(new ReviewResponse(1L, 10L, 2L, 3, "Bueno"));

        mockMvc.perform(patch("/api/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rating\":3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(3));
    }

    @Test
    @DisplayName("DELETE /api/reviews/{id} -> 204")
    void delete_devuelve204() throws Exception {
        mockMvc.perform(delete("/api/reviews/1"))
                .andExpect(status().isNoContent());
    }
}
