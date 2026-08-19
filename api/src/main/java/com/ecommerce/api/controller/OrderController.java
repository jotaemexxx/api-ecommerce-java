package com.ecommerce.api.controller;


import com.ecommerce.api.dto.OrderItemResponseDto;
import com.ecommerce.api.dto.OrderResponseDto;
import com.ecommerce.api.model.Order;
import com.ecommerce.api.security.UserPrincipal;
import com.ecommerce.api.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) { this.orderService = orderService;}

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long orderId, @AuthenticationPrincipal UserPrincipal principal) {
        Order order = orderService.getOrderById(orderId, principal.getId(), principal.getRole());
        return ResponseEntity.ok(toOrderResponseDto(order));
    }

    @GetMapping("/user")
    public ResponseEntity<List<OrderResponseDto>> getOrdersFromUser(@AuthenticationPrincipal UserPrincipal principal) {
        List<Order> orders = orderService.getOrdersFromUser(principal.getId());
        return ResponseEntity.ok(orders.stream().map(this::toOrderResponseDto).toList());
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders.stream().map(this::toOrderResponseDto).toList());
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDto> checkout(@AuthenticationPrincipal UserPrincipal principal) {
        Order order = orderService.checkout(principal.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(toOrderResponseDto(order));

    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponseDto> cancelOrder(@PathVariable Long orderId, @AuthenticationPrincipal UserPrincipal principal) {
        Order order = orderService.cancelOrder(orderId, principal.getId(), principal.getRole());
        return ResponseEntity.ok().body(toOrderResponseDto(order));
    }

    private OrderResponseDto toOrderResponseDto(Order order) {
        return new OrderResponseDto(order.getUser().getId(), order.getId(), order.getOrderItems().stream().map(orderItem -> new OrderItemResponseDto(orderItem.getProductName(), orderItem.getPrice(), orderItem.getQuantity())).toList(), order.getTotal(), order.getOrderDate(), order.getOrderStatus());
    }

}
