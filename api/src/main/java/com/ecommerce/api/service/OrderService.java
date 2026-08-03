package com.ecommerce.api.service;

import com.ecommerce.api.exception.EmptyCartException;
import com.ecommerce.api.exception.InsufficientStockException;
import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.model.Cart;
import com.ecommerce.api.model.CartItem;
import com.ecommerce.api.model.Order;
import com.ecommerce.api.model.Product;
import com.ecommerce.api.repository.CartRepository;
import com.ecommerce.api.repository.OrderRepository;
import com.ecommerce.api.repository.ProductRepository;
import com.ecommerce.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public Order getOrderById(Long orderId){
        return orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("pedido nao encontrado"));
    }

    public List<Order> getOrdersFromUser(Long userId){
        return orderRepository.findOrdersByUserId(userId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order checkout(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("carrinho nao encontrado"));
        if(cart.getItensCart().isEmpty()){
            throw new EmptyCartException("o carrinho está vazio");
        }

        for(CartItem item : cart.getItensCart()) {

            Product product = item.getProduct();

            if(item.getQuantity() > product.getStockQuantity()) {
                throw new InsufficientStockException("estoque insuficiente para o carrinho");
            }
        }

    }


}
