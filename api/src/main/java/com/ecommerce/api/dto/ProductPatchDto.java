package com.ecommerce.api.dto;


import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class ProductPatchDto {

    private String name;

    @Positive(message = "o preço deve ser um valor positivo")
    @Digits(integer = 10, fraction = 2, message = "o preço deve ter no máximo 2 casas decimais")
    private BigDecimal price;

    @PositiveOrZero(message = "a quantidade no estoque deve ser maior ou igual a zero")
    private Integer stockQuantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
