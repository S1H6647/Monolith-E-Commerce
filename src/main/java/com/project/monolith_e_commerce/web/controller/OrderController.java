package com.project.monolith_e_commerce.web.controller;

import com.project.monolith_e_commerce.security.user.UserPrincipal;
import com.project.monolith_e_commerce.service.OrderService;
import com.project.monolith_e_commerce.web.dto.order.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles order lifecycle operations.
 *
 * <p>Customer endpoints:
 * <ul>
 *   <li>POST /api/orders              — place a new order from the current cart</li>
 *   <li>GET  /api/orders              — list the current user's order history</li>
 *   <li>GET  /api/orders/{id}         — get a single order by id</li>
 * </ul>
 *
 * <p>Admin endpoints:
 * <ul>
 *   <li>GET  /api/admin/orders        — list all orders across all users (ADMIN)</li>
 * </ul>
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Place a new order from the current user's cart.
     */
    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> placeOrder(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(userPrincipal));
    }

    /**
     * Get the current user's order history.
     */
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(orderService.getMyOrder(userPrincipal));
    }

    /**
     * Get a single order by id.
     */
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponse> getOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(orderService.getOrderById(id, userPrincipal));
    }

    /**
     * Cancel an order for the current customer.
     */
    @PatchMapping("/orders/{id}/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(orderService.cancelOrder(id, userPrincipal));
    }

    /**
     * List all orders across all users. ADMIN only.
     */
    @GetMapping("/admin/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}
