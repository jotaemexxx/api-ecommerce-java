package com.ecommerce.api.controller;

import com.ecommerce.api.dto.AddItemRequestDto;
import com.ecommerce.api.dto.CartItemResponseDto;
import com.ecommerce.api.dto.CartResponseDto;
import com.ecommerce.api.dto.UpdateQuantityDto;
import com.ecommerce.api.model.CartItem;
import com.ecommerce.api.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService){ this.cartService = cartService;}

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponseDto>  getCartFromUser(@PathVariable Long userId){
        CartResponseDto response = cartService.getCartResponseByUserId(userId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<CartItemResponseDto> addProductOnCart(@PathVariable Long userId, @Valid @RequestBody AddItemRequestDto requestProductToAdd){
        CartItem cartItem = cartService.addProductToCart(userId, requestProductToAdd.getProductId(), requestProductToAdd.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(toCartItemResponseDto(cartItem));

    }

    @PutMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartItemResponseDto> updateProductQuantityOnCart(@PathVariable Long userId, @PathVariable Long productId, @Valid @RequestBody UpdateQuantityDto requestProductToUpdate) {
        CartItem cartItem = cartService.updateItemQuantity(userId, productId, requestProductToUpdate.getQuantity());
        return ResponseEntity.status(HttpStatus.OK).body(toCartItemResponseDto(cartItem));

    }

    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<Void> removeProductFromCart(@PathVariable Long userId, @PathVariable Long productId){
        cartService.removeProductFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/items/{productId}/increment")
    public ResponseEntity<CartItemResponseDto> incrementProductFromCart(@PathVariable Long userId, @PathVariable Long productId, @Valid @RequestBody UpdateQuantityDto request){
        CartItem cartItem = cartService.itemOnCartIncrement(userId, productId, request.getQuantity());
        return ResponseEntity.status(HttpStatus.OK).body(toCartItemResponseDto(cartItem));
    }

    @PatchMapping("/{userId}/items/{productId}/decrement")
    public ResponseEntity<CartItemResponseDto> decrementProductFromCart(@PathVariable Long userId, @PathVariable Long productId, @Valid @RequestBody UpdateQuantityDto request){
        CartItem cartItem = cartService.itemOnCartDecrement(userId, productId, request.getQuantity());
        return ResponseEntity.status(HttpStatus.OK).body(toCartItemResponseDto(cartItem));
    }

    private CartItemResponseDto toCartItemResponseDto(CartItem cartItem){
        return new CartItemResponseDto(cartItem.getProduct().getId(), cartItem.getProduct().getName(), cartItem.getQuantity(), cartItem.getPrice(), cartItem.calculateSubtotal());
    }

}
