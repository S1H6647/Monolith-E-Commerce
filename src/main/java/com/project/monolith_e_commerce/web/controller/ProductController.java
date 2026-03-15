package com.project.monolith_e_commerce.web.controller;

import com.project.monolith_e_commerce.service.ProductService;
import com.project.monolith_e_commerce.web.dto.product.CreateProductRequest;
import com.project.monolith_e_commerce.web.dto.product.ProductResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    /**
     * List all products, paginated. Optionally filter by {@code categoryId}.
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(
            @RequestParam(required = false, defaultValue = "1")
            @Min(1) int pageNo,

            @RequestParam(required = false, defaultValue = "8")
            @Min(1) @Max(50) int pageSize,

            @RequestParam(required = false, defaultValue = "name")
            String sortBy,

            @RequestParam(required = false, defaultValue = "ASC")
            String sortDirection,

            @RequestParam(required = false) Long categoryId,

            @RequestParam(required = false) String search
    ) {
        Sort sort;
        sort = "ASC".equalsIgnoreCase(sortDirection)
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        var response = productService.getAllProducts(pageable, categoryId, search);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a single product by its id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    /**
     * Create a new product. ADMIN only.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestPart("data") CreateProductRequest request,
            @RequestPart("image") MultipartFile image
    ) {
        var response = productService.createProduct(request, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update an existing product by id. ADMIN only.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestPart("data") CreateProductRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        var response = productService.updateProduct(id, request, image);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a product by id. ADMIN only.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
