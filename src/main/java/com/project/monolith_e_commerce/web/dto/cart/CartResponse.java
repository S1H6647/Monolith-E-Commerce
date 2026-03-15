package com.project.monolith_e_commerce.web.dto.cart;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
        Long id,
        List<CartItemResponse> items,
        BigDecimal totalPrice
) {
}

