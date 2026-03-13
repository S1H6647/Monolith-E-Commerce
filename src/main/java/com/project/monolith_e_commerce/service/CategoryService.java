package com.project.monolith_e_commerce.service;

import com.project.monolith_e_commerce.domain.product.Category;
import com.project.monolith_e_commerce.repository.CategoryRepository;
import com.project.monolith_e_commerce.web.dto.category.CategoryRequest;
import com.project.monolith_e_commerce.web.dto.category.CategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryResponse createCategory(@Valid CategoryRequest request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("Category already exists");
        }

        Category saved = categoryRepository.save(
                Category.builder()
                        .name(request.name())
                        .build()
        );

        return CategoryResponse.from(saved);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
