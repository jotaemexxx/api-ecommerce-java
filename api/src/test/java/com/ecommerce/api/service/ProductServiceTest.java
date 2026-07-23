package com.ecommerce.api.service;

import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.model.Product;
import com.ecommerce.api.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void deveRetornarListadeProdutos() {

        Product product1 = new Product("teclado", 235.5, 10);
        Product product2 = new Product("mouse vxe", 55.78, 36);
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        List<Product> resultado = productService.getAllProducts();

        assertEquals(2, resultado.size());
    }

    @Test
    void deveRetornarProdutoIdExiste(){
        Product product1 = new Product("teste", 25.67, 34);
        product1.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        Product resultado = productService.getProductById(1L);

        assertEquals("teste", resultado.getName());

    }

    @Test
    void deveRetornarProdutoInexistente(){
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {productService.getProductById(999L);});
    }

    @Test
    void deveSalvarProduto(){
        Product enterProduct = new Product("espatula", 445.5, 23);
        Product outProduct = new Product("espatula", 445.5, 23);
        outProduct.setId(1L);

        when(productRepository.save(enterProduct)).thenReturn(outProduct);

        Product resultado = productService.createProduct(enterProduct);

        assertEquals(1L, resultado.getId());
    }

    @Test
    void deveAtualizarProduto(){
        Product beforeProduct = new Product("nome", 34.2, 12);
        Product updateProduct =new Product("nomeAtualizado", 34.2, 12);
        Product savedProduct = new Product("nomeAtualizado", 34.2, 12);
        beforeProduct.setId(1L);
        savedProduct.setId(1l);

        when(productRepository.findById(1L)).thenReturn(Optional.of(beforeProduct));
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product resultado = productService.updateProduct(1L, updateProduct);

        assertEquals("nomeAtualizado", resultado.getName());

    }

    @Test
    void deveDeletarProduto(){

        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteProduct(1L);

        verify(productRepository).deleteById(1L);

    }

    @Test
    void naoDeveDeletarProduto(){

        when(productRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(999L));

        verify(productRepository, never()).deleteById(anyLong());
    }
}