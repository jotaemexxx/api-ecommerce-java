package com.ecommerce.api.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AddItemRequestDto {

    @NotNull
    private Long productId;

    @Positive
    private Integer quantity;

    public AddItemRequestDto(){}

    public AddItemRequestDto(Long productId, Integer quantity){
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
