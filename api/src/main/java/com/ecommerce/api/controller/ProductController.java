package com.ecommerce.api.controller;

import com.ecommerce.api.dto.ProductRequestDto;
import com.ecommerce.api.dto.ProductResponseDto;
import com.ecommerce.api.model.Product;
import com.ecommerce.api.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController
{
    private final ProductService productService;

    public ProductController(ProductService productService)
    {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts()
    {
        List<ProductResponseDto> response = productService.getAllProducts().stream().map(this::toResponseDto).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductBtId(@PathVariable Long id)
    {
        Product product = productService.getProductById(id);

        return ResponseEntity.ok(toResponseDto(product));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product)
    {
        Product savedProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PutMapping("{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDto requestDto) {
        Product productToUpdate = toEntity(requestDto);
        Product updatedProduct = productService.updateProduct(id, productToUpdate);
        return ResponseEntity.ok(toResponseDto(updatedProduct));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }

    private Product toEntity(ProductRequestDto dto) {
        return new Product(dto.getName(), dto.getPrice(), dto.getStockQuantity());
    }

    private ProductResponseDto toResponseDto(Product product) {
        return new ProductResponseDto(product.getId(), product.getName(), product.getPrice(), product.getStockQuantity());
    }

}


