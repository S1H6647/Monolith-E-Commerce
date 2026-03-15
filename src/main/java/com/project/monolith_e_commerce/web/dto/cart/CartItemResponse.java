package com.project.monolith_e_commerce.web.dto.cart;

import java.math.BigDecimal;

public record CartItemResponse(
        Long productId,
        String productName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
}
