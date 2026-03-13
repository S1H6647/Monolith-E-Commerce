package com.project.monolith_e_commerce.repository;

import com.project.monolith_e_commerce.domain.product.Category;
import com.project.monolith_e_commerce.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Product> findByCategoryIdAndNameContainingIgnoreCase(Long categoryId, String name, Pageable pageable);

    List<Product> findByNameContainingIgnoreCase(String name);

    Boolean existsProductByName(String name);

    boolean existsProductById(Long id);
}