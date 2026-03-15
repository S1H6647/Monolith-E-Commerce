package com.project.monolith_e_commerce.service;

import com.project.monolith_e_commerce.domain.cart.Cart;
import com.project.monolith_e_commerce.domain.order.Order;
import com.project.monolith_e_commerce.domain.order.OrderItem;
import com.project.monolith_e_commerce.domain.order.OrderStatus;
import com.project.monolith_e_commerce.domain.payment.PaymentStatus;
import com.project.monolith_e_commerce.domain.product.Inventory;
import com.project.monolith_e_commerce.domain.user.User;
import com.project.monolith_e_commerce.exception.InsufficientStockException;
import com.project.monolith_e_commerce.exception.ResourceNotFoundException;
import com.project.monolith_e_commerce.repository.CartRepository;
import com.project.monolith_e_commerce.repository.InventoryRepository;
import com.project.monolith_e_commerce.repository.OrderRepository;
import com.project.monolith_e_commerce.repository.PaymentRepository;
import com.project.monolith_e_commerce.security.user.UserPrincipal;
import com.project.monolith_e_commerce.web.dto.order.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final PaymentRepository paymentRepository;
    private final InventoryRepository inventoryRepository;

    @Transactional
    @PreAuthorize("hasRole('CUSTOMER')")
    public OrderResponse placeOrder(UserPrincipal userPrincipal) {
        User currentUser = userPrincipal.getUser();

        Cart cart = cartRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        Order order = Order.builder()
                .user(currentUser)
                .build();

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(i -> OrderItem.builder()
                        .order(order)
                        .product(i.getProduct())
                        .quantity(i.getQuantity())
                        .unitPrice(i.getProduct().getPrice())
                        .build())
                .toList();

        BigDecimal totalAmount = orderItems.stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        var saved = orderRepository.save(order);

        for (var cartItem : cart.getItems()) {
            var productId = cartItem.getProduct().getId();

            Inventory inventory = inventoryRepository.findByProductId(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product id: " + productId));

            if (inventory.getQuantity() < cartItem.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for product id: " + productId);
            }

            inventory.setQuantity(inventory.getQuantity() - cartItem.getQuantity());
            inventoryRepository.save(inventory);
        }
        cart.getItems().clear();

        return OrderResponse.from(saved);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    public List<OrderResponse> getMyOrder(UserPrincipal userPrincipal) {
        User currentUser = userPrincipal.getUser();

        var orderList = orderRepository.findByUserId(currentUser.getId());

        return orderList.stream()
                .map(OrderResponse::from)
                .toList();
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    public OrderResponse getOrderById(Long orderId, UserPrincipal userPrincipal) {
        User currentUser = userPrincipal.getUser();

        var order = orderRepository.findByIdAndUserId(orderId, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for id: " + orderId));

        return OrderResponse.from(order);
    }

    @Transactional
    @PreAuthorize("hasRole('CUSTOMER')")
    public OrderResponse cancelOrder(Long orderId, UserPrincipal userPrincipal) {
        User currentUser = userPrincipal.getUser();

        Order order = orderRepository.findByIdAndUserId(orderId, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for id: " + orderId));

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Delivered orders cannot be cancelled");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            return OrderResponse.from(order);
        }

        order.setStatus(OrderStatus.CANCELLED);

        for (var orderItem : order.getItems()) {
            var productId = orderItem.getProduct().getId();

            Inventory inventory = inventoryRepository.findByProductId(productId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Inventory not found for product id: " + productId
                    ));

            inventory.setQuantity(inventory.getQuantity() + orderItem.getQuantity());
            inventoryRepository.save(inventory);
        }


        orderRepository.save(order);

        paymentRepository.findByOrderId(order.getId()).ifPresent(payment -> {
            if (payment.getStatus() == PaymentStatus.PENDING || payment.getStatus() == PaymentStatus.COMPLETED) {
                payment.setStatus(PaymentStatus.FAILED);
                payment.setPaidAt(null);
                paymentRepository.save(payment);
            }
        });

        return OrderResponse.from(order);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .toList();
    }
}
