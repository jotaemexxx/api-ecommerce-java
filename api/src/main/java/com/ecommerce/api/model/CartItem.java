package com.ecommerce.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cartItens")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCartItem;


    private Cart cart;

    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double price;


    public CartItem() {

    }

    public CartItem(Long idCartItem, Cart cart, Product product, Integer quantity) {
        this.idCartItem = idCartItem;
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.price = product.getPrice();
    }

    public Long getIdCartItem() {
        return idCartItem;
    }

    public void setIdCardItem(Long idCartItem) {
        this.idCartItem = idCartItem;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }
}