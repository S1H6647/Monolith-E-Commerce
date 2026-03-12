package com.project.monolith_e_commerce.web.controller;

import com.project.monolith_e_commerce.domain.order.Order;
import com.project.monolith_e_commerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /** Place a new order from the current user's cart. */
    @PostMapping("/api/orders")
    public ResponseEntity<Order> placeOrder() {
        return null;
    }

    /** Get the current user's order history. */
    @GetMapping("/api/orders")
    public ResponseEntity<List<Order>> getMyOrders() {
        return null;
    }

    /** Get a single order by id. */
    @GetMapping("/api/orders/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return null;
    }

    /** List all orders across all users. ADMIN only. */
    @GetMapping("/api/admin/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getAllOrders() {
        return null;
    }
}
