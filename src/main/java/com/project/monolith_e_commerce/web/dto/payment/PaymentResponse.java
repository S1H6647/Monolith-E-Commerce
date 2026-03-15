package com.project.monolith_e_commerce.web.dto.payment;

import com.project.monolith_e_commerce.domain.payment.Payment;
import com.project.monolith_e_commerce.domain.payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResponse(
        Long id,
        Long orderId,
        PaymentStatus status,
        BigDecimal amount,
        Instant paidAt
) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getStatus(),
                payment.getAmount(),
                payment.getPaidAt()
        );
    }
}

