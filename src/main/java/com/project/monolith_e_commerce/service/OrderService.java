package com.project.monolith_e_commerce.service;

import com.project.monolith_e_commerce.repository.OrderRepository;
import com.project.monolith_e_commerce.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
}

