package com.ecommerce.api.dto;

public class UpdateQuantityDto {

    private Integer quantity;
    public UpdateQuantityDto() {}

    public UpdateQuantityDto(Integer quantity){
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
