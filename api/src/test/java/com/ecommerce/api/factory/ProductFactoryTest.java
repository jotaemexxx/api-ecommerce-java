package com.ecommerce.api.factory;

import com.ecommerce.api.model.Product;

public final class ProductFactoryTest {

    private ProductFactoryTest() {}

    public static Product createProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Produto-Test");
        product.setPrice(15.50);
        product.setStockQuantity(50);

        return product;
    }

    public static Product createProduct(Long id, String name, Double price, Integer quantity) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        product.setStockQuantity(quantity);

        return product;
    }


}
