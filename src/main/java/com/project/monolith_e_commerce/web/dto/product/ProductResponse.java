package com.project.monolith_e_commerce.web.dto.product;

import com.project.monolith_e_commerce.domain.product.Product;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String imageUrl,
        String categoryName,
        int stock,
        Instant createdAt
) {
    public static ProductResponse from(Product product, int stock) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory() != null ? product.getCategory().getName() : null,
                stock,
                product.getCreatedAt()
        );
    }

    public static ProductResponse from(Product product) {
        return from(product, 0);
    }
}

