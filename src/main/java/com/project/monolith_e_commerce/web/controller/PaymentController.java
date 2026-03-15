package com.project.monolith_e_commerce.web.controller;

import com.project.monolith_e_commerce.security.user.UserPrincipal;
import com.project.monolith_e_commerce.service.PaymentService;
import com.project.monolith_e_commerce.web.dto.payment.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Handles payment operations tied to an order.
 *
 * <ul>
 *   <li>POST /api/payments/{orderId}/pay — trigger payment processing for an order</li>
 *   <li>GET  /api/payments/{orderId}     — get the payment status for an order</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Trigger payment processing for the given order.
     */
    @PostMapping("/{orderId}/pay")
    public ResponseEntity<PaymentResponse> pay(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(paymentService.pay(orderId, userPrincipal));
    }

    /**
     * Get the current payment status for the given order.
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentStatus(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(paymentService.getPaymentStatus(orderId, userPrincipal));
    }
}

