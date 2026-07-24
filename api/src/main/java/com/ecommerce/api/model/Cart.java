package com.ecommerce.api.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import org.hibernate.mapping.Value;

import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private User user;

    private List<CartItem> itensCart;

    public Cart() {

    }

    public Cart(Long id, User user, List<CartItem> itensCart){
        this.id = id;
        this.user = user;
        this.itensCart = itensCart;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartItem> getItensCart() {
        return itensCart;
    }

    public void setItensCart(List<CartItem> itensCart) {
        this.itensCart = itensCart;
    }
}

