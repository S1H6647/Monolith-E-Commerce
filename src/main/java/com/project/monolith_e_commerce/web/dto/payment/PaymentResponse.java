package com.project.monolith_e_commerce.web.dto.payment;

import com.project.monolith_e_commerce.domain.payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResponse(
        Long id,
        Long orderId,
        PaymentStatus status,
        BigDecimal amount,
        Instant paidAt
) {}

