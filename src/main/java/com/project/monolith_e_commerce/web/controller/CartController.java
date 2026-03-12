package com.project.monolith_e_commerce.web.controller;

import com.project.monolith_e_commerce.service.CartService;
import com.project.monolith_e_commerce.web.dto.cart.AddToCartRequest;
import com.project.monolith_e_commerce.web.dto.cart.CartResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles shopping cart operations for the authenticated user.
 *
 * <ul>
 *   <li>GET    /api/cart                   — get the current user's cart</li>
 *   <li>POST   /api/cart/items             — add an item to the cart</li>
 *   <li>PUT    /api/cart/items/{productId} — update the quantity of an existing item</li>
 *   <li>DELETE /api/cart/items/{productId} — remove a specific item from the cart</li>
 *   <li>DELETE /api/cart                   — clear the entire cart</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /** Get the current authenticated user's cart. */
    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        return null;
    }

    /** Add a product item to the cart. */
    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(@Valid @RequestBody AddToCartRequest request) {
        return null;
    }

    /** Update the quantity of an existing cart item by product id. */
    @PutMapping("/items/{productId}")
    public ResponseEntity<CartResponse> updateItem(
            @PathVariable Long productId,
            @RequestParam @Positive int quantity) {
        return null;
    }

    /** Remove a specific item from the cart by product id. */
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponse> removeItem(@PathVariable Long productId) {
        return null;
    }

    /** Clear all items from the cart. */
    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        return null;
    }
}
