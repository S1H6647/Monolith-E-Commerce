package com.project.monolith_e_commerce.service;

import com.project.monolith_e_commerce.repository.CartRepository;
import com.project.monolith_e_commerce.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
}

