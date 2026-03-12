package com.project.monolith_e_commerce.web.controller;

import com.project.monolith_e_commerce.service.ProductService;
import com.project.monolith_e_commerce.web.dto.product.CreateProductRequest;
import com.project.monolith_e_commerce.web.dto.product.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Handles product catalogue operations.
 * Public endpoints are readable by anyone; write operations are restricted to ADMIN.
 *
 * <ul>
 *   <li>GET    /api/products          — list all products, paginated, filterable by categoryId</li>
 *   <li>GET    /api/products/{id}     — get a single product by id</li>
 *   <li>POST   /api/products          — create a new product (ADMIN)</li>
 *   <li>PUT    /api/products/{id}     — update an existing product (ADMIN)</li>
 *   <li>DELETE /api/products/{id}     — delete a product (ADMIN)</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /** List all products, paginated. Optionally filter by {@code categoryId}. */
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            Pageable pageable,
            @RequestParam(required = false) Long categoryId) {
        return null;
    }

    /** Get a single product by its id. */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        return null;
    }

    /** Create a new product. ADMIN only. */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        return null;
    }

    /** Update an existing product by id. ADMIN only. */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody CreateProductRequest request) {
        return null;
    }

    /** Delete a product by id. ADMIN only. */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        return null;
    }
}
