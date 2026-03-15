package com.project.monolith_e_commerce.service;

import com.project.monolith_e_commerce.domain.order.Order;
import com.project.monolith_e_commerce.domain.order.OrderStatus;
import com.project.monolith_e_commerce.domain.payment.Payment;
import com.project.monolith_e_commerce.domain.payment.PaymentStatus;
import com.project.monolith_e_commerce.domain.user.User;
import com.project.monolith_e_commerce.exception.ResourceNotFoundException;
import com.project.monolith_e_commerce.repository.OrderRepository;
import com.project.monolith_e_commerce.repository.PaymentRepository;
import com.project.monolith_e_commerce.security.user.UserPrincipal;
import com.project.monolith_e_commerce.web.dto.payment.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentResponse pay(Long orderId, UserPrincipal userPrincipal) {
        User user = userPrincipal.getUser();

        Order order = orderRepository.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No order found for id: " + orderId));

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseGet(() -> Payment.builder()
                        .order(order)
                        .amount(order.getTotalAmount())
                        .status(PaymentStatus.PENDING)
                        .build());

        if (order.getStatus() == OrderStatus.CANCELLED) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setPaidAt(null);
            Payment saved = paymentRepository.save(payment);
            return PaymentResponse.from(saved);
        }

        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            return PaymentResponse.from(payment);
        }

        boolean success = orderId % 5 != 0;

        if (success) {
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setPaidAt(Instant.now());
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setPaidAt(null);
        }

        Payment saved = paymentRepository.save(payment);
        return PaymentResponse.from(saved);
    }

    public PaymentResponse getPaymentStatus(Long orderId, UserPrincipal userPrincipal) {
        User user = userPrincipal.getUser();

        orderRepository.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No order found for id: " + orderId));

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("No payment found for order id: " + orderId));

        return PaymentResponse.from(payment);
    }
}

