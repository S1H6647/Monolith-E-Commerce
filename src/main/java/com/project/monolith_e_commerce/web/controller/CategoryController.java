package com.project.monolith_e_commerce.web.controller;

import com.project.monolith_e_commerce.domain.product.Category;
import com.project.monolith_e_commerce.service.CategoryService;
import com.project.monolith_e_commerce.web.dto.category.CategoryRequest;
import com.project.monolith_e_commerce.web.dto.category.CategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles product category operations.
 *
 * <ul>
 *   <li>GET    /api/categories        — list all categories</li>
 *   <li>GET    /api/categories/{id}   — get a single category by id</li>
 *   <li>POST   /api/categories        — create a new category (ADMIN)</li>
 *   <li>DELETE /api/categories/{id}   — delete a category (ADMIN)</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * List all available categories.
     */
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    /**
     * Create a new category. ADMIN only.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(request));
    }
}
