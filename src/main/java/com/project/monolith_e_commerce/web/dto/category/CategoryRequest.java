package com.project.monolith_e_commerce.web.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank String name
) {
}

