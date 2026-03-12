package com.project.monolith_e_commerce.service;

import com.project.monolith_e_commerce.repository.ProductRepository;
import com.project.monolith_e_commerce.repository.CategoryRepository;
import com.project.monolith_e_commerce.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;
}

