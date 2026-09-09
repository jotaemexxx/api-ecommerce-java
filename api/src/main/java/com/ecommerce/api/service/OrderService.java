package com.ecommerce.api.service;

import com.ecommerce.api.exception.*;
import com.ecommerce.api.model.*;
import com.ecommerce.api.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;


    public OrderService(OrderRepository orderRepository, CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;

    }

    public Order getOrderById(Long orderId, Long requesterId, Role requesterRole){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("pedido nao encontrado"));

        boolean isOwner = order.getUser().getId().equals(requesterId);
        boolean isAdmin = requesterRole == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new DeniedAccessException("acesso negado");
        }

        return order;
    }

    public List<Order> getOrdersFromUser(Long userId){
        return orderRepository.findOrdersByUserId(userId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllOrdersWithItemsAndUser();
    }

    @Transactional
    public Order checkout(Long userId) {

        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() -> new ResourceNotFoundException("carrinho nao encontrado"));

        if (cart.getItensCart().isEmpty()) {
            throw new EmptyCartException("o carrinho está vazio");
        }


        for (CartItem item : cart.getItensCart()) {

            if (item.getQuantity() > item.getProduct().getStockQuantity()) {
                throw new InsufficientStockException(
                        "Estoque insuficiente para o produto: "
                                + item.getProduct().getName()
                );
            }
        }


        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();


        for (CartItem item : cart.getItensCart()) {

            OrderItem orderItem = new OrderItem(
                    order,
                    item.getProduct(),
                    item.getProduct().getName(),
                    item.getPrice(),
                    item.getQuantity()
            );

            orderItems.add(orderItem);

            item.getProduct().setStockQuantity(
                    item.getProduct().getStockQuantity() - item.getQuantity()
            );
        }

        order.setOrderItems(orderItems);

        order.setTotal(
                orderItems.stream()
                        .map(OrderItem::calculateSubtotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .setScale(2, RoundingMode.HALF_UP)
        );

        order.setOrderStatus(Order.OrderStatus.PENDING);

        order = orderRepository.save(order);

        cart.getItensCart().clear();

        return order;
    }

    @Transactional
    public Order cancelOrder(Long id, Long requesterId, Role requesterRole) {
        Order order = orderRepository.findByIdWithItems(id).orElseThrow(() -> new ResourceNotFoundException("pedido nao encontrado"));

        boolean isOwner = order.getUser().getId().equals(requesterId);
        boolean isAdmin = requesterRole == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new DeniedAccessException("acesso negado");
        }


        if(order.getOrderStatus() == Order.OrderStatus.CANCELLED){
            throw new CancelledOrderException("o pedido já foi cancelado");
        }

        if(order.getOrderStatus() == Order.OrderStatus.PAID){
            throw new PaidOrderException("o pedido já foi pago");
        }

        for(OrderItem item : order.getOrderItems()){
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
        }

        order.setOrderStatus(Order.OrderStatus.CANCELLED);

        return orderRepository.save(order);
    }

}
