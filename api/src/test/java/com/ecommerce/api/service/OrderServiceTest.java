package com.ecommerce.api.service;

import com.ecommerce.api.exception.*;
import com.ecommerce.api.factory.CartFactory;
import com.ecommerce.api.factory.OrderFactory;
import com.ecommerce.api.factory.ProductFactory;
import com.ecommerce.api.model.*;
import com.ecommerce.api.repository.CartItemRepository;
import com.ecommerce.api.repository.CartRepository;
import com.ecommerce.api.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void deveRetornarPedidoPeloId() {
        Order order = OrderFactory.createOrder();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order resultado = orderService.getOrderById(1L);

        assertEquals(1L, resultado.getId());
    }

    @Test
    void deveRetornarPedidosDoUsuario() {
        Order order = OrderFactory.createOrder();

        when(orderRepository.findOrdersByUserId(1L)).thenReturn(List.of(order));

        List<Order> resultado = orderService.getOrdersFromUser(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void deveRetornarTodosOsPedidos() {
        Order order1 = OrderFactory.createOrder();
        Order order2 = OrderFactory.createOrder();

        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));

        List<Order> resultado = orderService.getAllOrders();

        assertEquals(2, resultado.size());
    }

    @Test
    void checkoutComSucesso() {
        Product product = ProductFactory.createProduct();
        Cart cart = CartFactory.createCart();
        CartItem cartItem = CartFactory.createCartItem(cart, product, 5);
        cart.getItensCart().add(cartItem);

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenReturn(OrderFactory.createOrder());

        Order resultado = orderService.checkout(1L);

        assertEquals(Order.OrderStatus.PENDING, resultado.getOrderStatus());
    }

    @Test
    void checkoutFalhaCarrinhoVazio() {
        Cart cart = CartFactory.createCart();

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        assertThrows(EmptyCartException.class, () -> {
            orderService.checkout(1L);
        });
    }

    @Test
    void checkoutFalhaComCarrinhoInexistente() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {orderService.checkout(1L);});
    }

    @Test
    void cancelarOrdemInexistente() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.cancelOrder(1L));
    }

    @Test
    void checkoutFalhaEstoqueInsuficiente() {
        Product product = ProductFactory.createProduct();
        Cart cart = CartFactory.createCart();
        CartItem cartItem = CartFactory.createCartItem(cart, product, 51);
        cart.getItensCart().add(cartItem);

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        assertThrows(InsufficientStockException.class, () -> {
            orderService.checkout(1L);
        });
    }

    @Test
    void cancelarOrderComSucesso() {
        Order order = OrderFactory.createOrder();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order resultado = orderService.cancelOrder(1L);

        assertEquals(Order.OrderStatus.CANCELLED, resultado.getOrderStatus());
    }

    @Test
    void cancelarOrderFalhaPedidoJaCancelado() {
        Order order = OrderFactory.createOrder();
        order.setOrderStatus(Order.OrderStatus.CANCELLED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(CancelledOrderException.class, () -> orderService.cancelOrder(1L));
    }

    @Test
    void cancelarOrderFalhaPedidoJaPago() {
        Order order = OrderFactory.createOrder();
        order.setOrderStatus(Order.OrderStatus.PAID);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(PaidOrderException.class, () -> orderService.cancelOrder(1L));
    }
}

