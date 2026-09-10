package com.ecommerce.api.dto;

import java.math.BigDecimal;

public class OrderItemResponseDto {

    private String productName;
    private BigDecimal price;
    private Integer quantity;

    public OrderItemResponseDto() {}

    public OrderItemResponseDto(String productName, BigDecimal price, Integer quantity) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
