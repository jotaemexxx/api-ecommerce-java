package com.ecommerce.api.factory;

import com.ecommerce.api.model.Cart;
import com.ecommerce.api.model.CartItem;
import com.ecommerce.api.model.Product;
import com.ecommerce.api.model.User;

import java.util.ArrayList;
import java.util.List;

public final class CartFactory {

    private CartFactory() {}

    public static Cart createCart() {
        User user = new User("joao", "joao@gmail.com", "6993204040", "senha123*");
        user.setId(1L);

        Cart cart = new Cart(user, new ArrayList<>());
        cart.setId(1L);
        return cart;
    }

    public static Cart createCart(User user) {
        Cart cart = new Cart(user, new ArrayList<>());
        cart.setId(1L);
        return cart;
    }

    public static Cart createCartWithItems(User user, List<CartItem> items) {
        Cart cart = new Cart(user, items);
        cart.setId(1L);
        return cart;
    }

    public static CartItem createCartItem(Cart cart, Product product, Integer quantity) {
        return new CartItem(cart, product, quantity);
    }
}