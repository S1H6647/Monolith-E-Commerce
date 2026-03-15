package com.project.monolith_e_commerce.service;

import com.project.monolith_e_commerce.domain.cart.Cart;
import com.project.monolith_e_commerce.domain.cart.CartItem;
import com.project.monolith_e_commerce.domain.product.Product;
import com.project.monolith_e_commerce.domain.user.Role;
import com.project.monolith_e_commerce.domain.user.User;
import com.project.monolith_e_commerce.exception.InsufficientStockException;
import com.project.monolith_e_commerce.exception.ResourceNotFoundException;
import com.project.monolith_e_commerce.repository.CartItemRepository;
import com.project.monolith_e_commerce.repository.CartRepository;
import com.project.monolith_e_commerce.repository.InventoryRepository;
import com.project.monolith_e_commerce.repository.ProductRepository;
import com.project.monolith_e_commerce.security.user.UserPrincipal;
import com.project.monolith_e_commerce.web.dto.cart.AddToCartRequest;
import com.project.monolith_e_commerce.web.dto.cart.CartItemResponse;
import com.project.monolith_e_commerce.web.dto.cart.CartResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Transactional
    public CartResponse addItem(@Valid AddToCartRequest request, UserPrincipal userPrincipal) throws AccessDeniedException {
        User currentUser = userPrincipal.getUser();

        if (!currentUser.getRole().equals(Role.CUSTOMER)) {
            throw new AccessDeniedException("Accessed denied");
        }

        Cart cart = cartRepository.findByUserId(currentUser.getId())
                .orElseGet(() -> cartRepository.save(Cart.builder().user(currentUser).build()));

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        int productQuantity = inventoryRepository.findByProductId(product.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found for product id: " + product.getId()
                )).getQuantity();

        if (productQuantity == 0) {
            throw new InsufficientStockException("0 stock for product id: " + product.getId());
        }

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), request.productId())
                .map(existing -> {
                    int requestedTotal = existing.getQuantity() + request.quantity();
                    if (requestedTotal > productQuantity) {
                        throw new InsufficientStockException("Insufficient stock for product id: " + product.getId());
                    }
                    existing.setQuantity(requestedTotal);
                    return existing;
                })
                .orElseGet(() -> CartItem.builder()
                        .cart(cart)
                        .product(product)
                        .quantity(Math.min(productQuantity, request.quantity()))
                        .build());

        cartItemRepository.save(item);
        return toCartResponse(cart);

    }

    public CartResponse getCart(UserPrincipal userPrincipal) {
        User currentUser = userPrincipal.getUser();

        if (!currentUser.getRole().equals(Role.CUSTOMER)) {
            throw new AccessDeniedException("Accessed denied");
        }

        Cart cart = cartRepository.findByUserId(currentUser.getId())
                .orElseGet(() -> cartRepository.save(Cart.builder().user(currentUser).build()));

        return toCartResponse(cart);
    }

    public CartResponse updateItem(
            Long productId,
            @Positive int quantity,
            UserPrincipal userPrincipal
    ) {
        User currentUser = userPrincipal.getUser();

        if (!currentUser.getRole().equals(Role.CUSTOMER)) {
            throw new AccessDeniedException("Accessed denied");
        }

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for id: " + productId));

        Cart cart = cartRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        int productQuantity = inventoryRepository.findByProductId(existingProduct.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found for product id: " + existingProduct.getId()
                )).getQuantity();

        if (quantity > productQuantity) {
            throw new InsufficientStockException("Insufficient stock for product id: " + existingProduct.getId());
        }

        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), existingProduct.getId())
                .map(p -> {
                    p.setQuantity(quantity);
                    return p;
                })
                .orElseGet(() -> CartItem.builder()
                        .cart(cart)
                        .product(existingProduct)
                        .quantity(quantity)
                        .build());

        cartItemRepository.save(item);
        return toCartResponse(cart);
    }

    public CartResponse deleteItem(Long productId, UserPrincipal userPrincipal) {
        User currentUser = userPrincipal.getUser();

        if (!currentUser.getRole().equals(Role.CUSTOMER)) {
            throw new AccessDeniedException("Accessed denied");
        }

        Cart cart = cartRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart is empty"));

        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        cartItemRepository.delete(item);
        return toCartResponse(cart);
    }

    @Transactional
    public void clearCart(UserPrincipal userPrincipal) {
        User currentUser = userPrincipal.getUser();

        if (!currentUser.getRole().equals(Role.CUSTOMER)) {
            throw new AccessDeniedException("Accessed denied");
        }

        Cart cart = cartRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart is empty"));

        cart.getItems().clear();
    }

    /*
     *  Helper method: toCartResponse
     */
    private CartResponse toCartResponse(Cart cart) {
        var items = cartItemRepository.findByCartId(cart.getId()).stream()
                .map(i -> {
                    var unitPrice = i.getProduct().getPrice();
                    var subtotal = unitPrice.multiply(BigDecimal.valueOf(i.getQuantity()));
                    return new CartItemResponse(
                            i.getProduct().getId(),
                            i.getProduct().getName(),
                            i.getQuantity(),
                            unitPrice,
                            subtotal
                    );
                })
                .toList();

        var totalPrice = items.stream()
                .map(CartItemResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(cart.getId(), items, totalPrice);
    }
}

