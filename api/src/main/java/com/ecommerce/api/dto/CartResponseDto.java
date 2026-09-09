package com.ecommerce.api.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartResponseDto {
    private Long cartId;
    private Long userId;
    private List<CartItemResponseDto> cartItems;
    private BigDecimal total;

    public CartResponseDto(){}

    public CartResponseDto(Long cartId, Long userId, List<CartItemResponseDto> cartItems, BigDecimal total) {
        this.cartId = cartId;
        this.userId = userId;
        this.cartItems = cartItems;
        this.total = total;

    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public List<CartItemResponseDto> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemResponseDto> cartItems) {
        this.cartItems = cartItems;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
