package com.ecommerce.api.service;

import com.ecommerce.api.exception.InsufficientStockException;
import com.ecommerce.api.exception.RemoveInvalidCartItemException;
import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.model.Cart;
import com.ecommerce.api.model.CartItem;
import com.ecommerce.api.model.Product;
import com.ecommerce.api.repository.CartItemRepository;
import com.ecommerce.api.repository.CartRepository;
import com.ecommerce.api.repository.ProductRepository;
import com.ecommerce.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.ecommerce.api.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;


    public CartService(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;

    }

    public Cart getCartByUserId(Long userId) {
         return cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("carrinho nao encontrado"));
    }

    @Transactional
    public CartItem addProductToCart(Long userId, Long productId, Integer quantity){
        User userExisting = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("usuario nao encontrado"));
        Product productExisting = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("o produto nao existe"));
        Cart cart = cartRepository.findByUserId(userExisting.getId()).orElseThrow(() -> new ResourceNotFoundException("o carrinho nao existe"));
        Optional<CartItem> cartItemOptional = cartItemRepository.findByCartAndProduct(cart, productExisting);

        CartItem cartItem = cartItemOptional
                .map(item -> {
                    item.setQuantity(item.getQuantity() + quantity);
                    return item;
                })
                .orElseGet(() -> new CartItem(cart, productExisting, quantity));

        return cartItemRepository.save(cartItem);

    }

    public void deleteProductOnCart(Long userId, Long productId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("usuario nao encontrado");
        }

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("produto nao encontrado"));
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("o carrinho nao existe"));
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, existingProduct)
                .orElseThrow(() -> new RemoveInvalidCartItemException("o item nao encontrado no carrinho"));

        cartItemRepository.delete(cartItem);
    }

    public CartItem updateItemQuantity(Long userId, Long productId, Integer quantity) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario nao encontrado");
        }

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("produto nao encontrado"));
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("o carrinho nao existe"));
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, existingProduct)
                .orElseThrow(() -> new ResourceNotFoundException("o item nao encontrado no carrinho"));

        if (quantity > existingProduct.getStockQuantity()) {
            throw new InsufficientStockException("quantidade indisponivel");
        }

        cartItem.setQuantity(quantity);

        return cartItemRepository.save(cartItem);
    }

}
