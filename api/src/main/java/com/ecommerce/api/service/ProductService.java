package com.ecommerce.api.service;

import com.ecommerce.api.dto.ProductPatchDto;
import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.model.Product;
import com.ecommerce.api.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductService
{
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts()
    {
        return productRepository.findAll();
    }
    public Product getProductById(Long id)
    {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto nao encontrado com id " + id));
    }

    public Product createProduct(Product product)
    {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id " + id));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStockQuantity(updatedProduct.getStockQuantity());

        return productRepository.save(existingProduct);
    }

    public Product partialUpdateProduct(Long id, ProductPatchDto productPatchDto) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto nao encontrado com id " + id));

        if(productPatchDto.getName() != null){
            existingProduct.setName(productPatchDto.getName());
        }
        if(productPatchDto.getPrice() != null){
            existingProduct.setPrice(productPatchDto.getPrice());
        }
        if(productPatchDto.getStockQuantity() != null){
            existingProduct.setStockQuantity(productPatchDto.getStockQuantity());
        }

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto nao encontrado com id " + id);
        }
        productRepository.deleteById(id);
    }
}