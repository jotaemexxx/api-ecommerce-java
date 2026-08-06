package com.ecommerce.api.factory;

import com.ecommerce.api.model.Order;
import com.ecommerce.api.model.OrderItem;
import com.ecommerce.api.model.Product;
import com.ecommerce.api.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class OrderFactory {

    private OrderFactory() {}

    public static Order createOrder() {
        User user = new User("joao", "joao@gmail.com", "6993204040", "senha123*");
        user.setId(1L);

        Order order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(Order.OrderStatus.PENDING);
        order.setOrderItems(new ArrayList<>());
        order.setTotal(0.0);
        return order;
    }

    public static Order createOrder(User user, List<OrderItem> orderItems, Double total, Order.OrderStatus status) {
        Order order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(status);
        order.setOrderItems(orderItems);
        order.setTotal(total);
        return order;
    }

    public static OrderItem createOrderItem(Order order, Product product, Integer quantity) {
        return new OrderItem(order, product, product.getName(), product.getPrice(), quantity);
    }
}