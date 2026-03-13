package com.project.monolith_e_commerce.service;

import com.project.monolith_e_commerce.domain.product.Category;
import com.project.monolith_e_commerce.domain.product.Inventory;
import com.project.monolith_e_commerce.domain.product.Product;
import com.project.monolith_e_commerce.exception.DuplicateProductException;
import com.project.monolith_e_commerce.exception.ResourceNotFoundException;
import com.project.monolith_e_commerce.repository.CategoryRepository;
import com.project.monolith_e_commerce.repository.InventoryRepository;
import com.project.monolith_e_commerce.repository.ProductRepository;
import com.project.monolith_e_commerce.web.dto.cloudinary.CloudinaryUploadResult;
import com.project.monolith_e_commerce.web.dto.product.CreateProductRequest;
import com.project.monolith_e_commerce.web.dto.product.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;
    private final StorageService storageService;

    public ProductResponse createProduct(
            @Valid CreateProductRequest request,
            MultipartFile image
    ) {

        if (productRepository.existsProductByName(request.name())) {
            throw new DuplicateProductException("Product already exists");
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Category of id %d not found", request.categoryId())));

        CloudinaryUploadResult imageUrl = storageService.uploadImage(image);

        Product saved = Product.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .imageUrl(imageUrl.secureUrl())
                .imagePublicId(imageUrl.publicId())
                .category(category)
                .build();

        Inventory inventory = Inventory.builder()
                .product(saved)
                .quantity(request.initialStock())
                .build();

        productRepository.save(saved);
        inventoryRepository.save(inventory);

        return ProductResponse.from(saved, inventory.getQuantity());
    }

}

