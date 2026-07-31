package com.ecommerce.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "orderItems")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "order_id")
    @ManyToOne
    private Order order;

    @JoinColumn(name = "product_id")
    @ManyToOne
    private Product product;

    private String productName;

    private Double price;

    private Integer quantity;

    public OrderItem() {}

    public OrderItem(Order order, Product product, String productName, Double price, Integer quantity) {
        this.order = order;
        this.product = product;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
