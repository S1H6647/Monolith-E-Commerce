package com.project.monolith_e_commerce.repository;

import com.project.monolith_e_commerce.domain.order.Order;
import com.project.monolith_e_commerce.domain.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(UUID userId);

    List<Order> findByUserIdAndStatus(UUID userId, OrderStatus status);

    List<Order> findByStatus(OrderStatus status);
    
    Optional<Order> findByIdAndUserId(Long id, UUID userId);
}