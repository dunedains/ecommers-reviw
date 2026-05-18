package com.ecommers.reviews.client;

import com.ecommers.reviews.dto.ReviewDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${feign.client.product-url}")
public interface ProductClient {

    @GetMapping("/api/productos/{id}")
    ReviewDto.ProductDto getProductById(@PathVariable Long id);
}
