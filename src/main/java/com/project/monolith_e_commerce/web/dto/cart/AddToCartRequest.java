package com.project.monolith_e_commerce.web.dto.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddToCartRequest(

        @NotNull
        Long productId,

        @Positive
        int quantity
) {}

