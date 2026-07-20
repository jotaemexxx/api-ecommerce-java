package com.ecommerce.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public class ProductRequestDto
{
    @NotBlank(message = "o nome do produto é obrigatório")
    private String name;

    @NotNull(message = "o preço é obrigatório")
    @Positive(message = "o preço deve ser maior que zero")
    private Double price;

    @NotNull(message = "a quantidade em estoque é obrigatória")
    @Positive(message = "a quantidade em estoque deve ser positiva")
    private Integer stockQuantity;

    public ProductRequestDto(){}

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name =  name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity)
    {
        this.stockQuantity = stockQuantity;
    }


}
