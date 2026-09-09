package com.ecommerce.api.dto;


import com.ecommerce.api.model.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDto {

    private Long userId;
    private Long orderId;
    private List<OrderItemResponseDto> orderItems;
    private BigDecimal total;
    private LocalDateTime orderDate;
    private Order.OrderStatus orderStatus;

    public OrderResponseDto() {}

    public OrderResponseDto(Long userId, Long orderId, List<OrderItemResponseDto> orderItems, BigDecimal total, LocalDateTime orderDate, Order.OrderStatus orderStatus) {
        this.userId = userId;
        this.orderId = orderId;
        this.orderItems = orderItems;
        this.total = total;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<OrderItemResponseDto> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemResponseDto> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Order.OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Order.OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
