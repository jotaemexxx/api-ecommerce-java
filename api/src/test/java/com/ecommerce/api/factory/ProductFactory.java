package com.ecommerce.api.factory;

import com.ecommerce.api.model.Product;

import java.math.BigDecimal;

public final class ProductFactory {

    private ProductFactory() {}

    public static Product createProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Produto-Test");
        product.setPrice(new BigDecimal("15.50"));
        product.setStockQuantity(50);

        return product;
    }

    public static Product createProduct(Long id, String name, BigDecimal price, Integer quantity) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        product.setStockQuantity(quantity);

        return product;
    }


}
