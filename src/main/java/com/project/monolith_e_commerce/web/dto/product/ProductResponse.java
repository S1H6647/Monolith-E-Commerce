package com.project.monolith_e_commerce.web.dto.product;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String categoryName,
        int stock,
        Instant createdAt
) {}

