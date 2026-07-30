package com.ecommerce.api.service;

import com.ecommerce.api.dto.CartItemResponseDto;
import com.ecommerce.api.dto.CartResponseDto;
import com.ecommerce.api.exception.InsufficientStockException;
import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.model.Cart;
import com.ecommerce.api.model.CartItem;
import com.ecommerce.api.model.Product;
import com.ecommerce.api.model.User;
import com.ecommerce.api.repository.CartItemRepository;
import com.ecommerce.api.repository.CartRepository;
import com.ecommerce.api.repository.ProductRepository;
import com.ecommerce.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest  {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void deveRetonarCarrinhoPeloUserID() {
        Product product = new Product("escova de dente", 15.35, 75);
        User user = new User("joao", "joao@gmail.com", "6993204040", "senha123*");
        user.setId(1L);

        Cart cart = new Cart(user, new ArrayList<>());
        CartItem cartItem = new CartItem(cart, product, 2);
        cart.getItensCart().add(cartItem);

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        CartResponseDto resultado = cartService.getCartResponseByUserId(1L);
        assertEquals("escova de dente", resultado.getCartItems().get(0).getProductName());

    }

    @Test
    void deveAdicionarProdutoNovoNoCarrinho() {
        Product product = new Product("escova de dente", 15.35, 75);
        User user = new User("joao", "joao@gmail.com", "6993204040", "senha123*");
        user.setId(1L);
        product.setId(1L);

        Cart cart = new Cart(user, new ArrayList<>());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartAndProduct(cart, product)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(new CartItem(cart, product, 5));

        CartItem resultado = cartService.addProductToCart(1L, 1l, 5);

        assertEquals("escova de dente", resultado.getProduct().getName());

    }

    @Test
    void deveAdicionarProdutoNoCarrinhoJaExistente() {
        Product product = new Product("escova de dente", 15.35, 75);
        User user = new User("joao", "joao@gmail.com", "6993204040", "senha123*");
        user.setId(1L);
        product.setId(1L);

        Cart cart = new Cart(user, new ArrayList<>());
        CartItem cartItem = new CartItem(cart, product, 5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartAndProduct(cart, product)).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        CartItem resultado = cartService.addProductToCart(1L, 1L,2);

        assertEquals(7, resultado.getQuantity());

    }

    @Test
    void deveRejeitarAdicaoDeProdutoNoCarrinhoPorEstoqueInsuficiente() {
        Product product = new Product("escova de dente", 15.35, 75);
        User user = new User("joao", "joao@gmail.com", "6993204040", "senha123*");
        user.setId(1L);
        product.setId(1L);

        Cart cart = new Cart(user, new ArrayList<>());
        CartItem cartItem = new CartItem(cart, product, 5);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartAndProduct(cart,product)).thenReturn(Optional.of(cartItem));


        assertThrows(InsufficientStockException.class, () -> {cartService.addProductToCart(1L,1L, 71);});

    }

    @Test
    void deveAtualizarQuantidadeDoProdutoNoCarrinho() {
        Product product = new Product("escova de dente", 15.35, 75);
        User user = new User("joao", "joao@gmail.com", "6993204040", "senha123*");
        user.setId(1L);
        product.setId(1L);

        Cart cart = new Cart(user, new ArrayList<>());
        CartItem cartItem = new CartItem(cart, product, 5);

        when(userRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartAndProduct(cart,product)).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        CartItem resultado = cartService.updateItemQuantity(1L,1L, 10);

        assertEquals(10, cartItem.getQuantity());

    }



}
