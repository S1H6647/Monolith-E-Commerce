package com.project.monolith_e_commerce.web.dto.category;

import com.project.monolith_e_commerce.domain.product.Category;

public record CategoryResponse(
        Long id,
        String name
) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName()
        );
    }
}

