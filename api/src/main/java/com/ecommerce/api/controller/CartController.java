package com.ecommerce.api.controller;

import com.ecommerce.api.dto.AddItemRequestDto;
import com.ecommerce.api.dto.CartItemResponseDto;
import com.ecommerce.api.dto.CartResponseDto;
import com.ecommerce.api.dto.UpdateQuantityDto;
import com.ecommerce.api.model.CartItem;
import com.ecommerce.api.security.UserPrincipal;
import com.ecommerce.api.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService){ this.cartService = cartService;}

    @GetMapping
    public ResponseEntity<CartResponseDto>  getCartFromUser(@AuthenticationPrincipal UserPrincipal principal){
        CartResponseDto response = cartService.getCartResponseByUserId(principal.getId());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/items")
    public ResponseEntity<CartItemResponseDto> addProductOnCart(@AuthenticationPrincipal UserPrincipal principal, @Valid @RequestBody AddItemRequestDto requestProductToAdd){
        CartItem cartItem = cartService.addProductToCart(principal.getId(), requestProductToAdd.getProductId(), requestProductToAdd.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(toCartItemResponseDto(cartItem));

    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<CartItemResponseDto> updateProductQuantityOnCart(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long productId, @Valid @RequestBody UpdateQuantityDto requestProductToUpdate) {
        CartItem cartItem = cartService.updateItemQuantity(principal.getId(), productId, requestProductToUpdate.getQuantity());
        return ResponseEntity.status(HttpStatus.OK).body(toCartItemResponseDto(cartItem));

    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeProductFromCart(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long productId){
        cartService.removeProductFromCart(principal.getId(), productId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/items/{productId}/increment")
    public ResponseEntity<CartItemResponseDto> incrementProductFromCart(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long productId, @Valid @RequestBody UpdateQuantityDto request){
        CartItem cartItem = cartService.itemOnCartIncrement(principal.getId(), productId, request.getQuantity());
        return ResponseEntity.status(HttpStatus.OK).body(toCartItemResponseDto(cartItem));
    }

    @PatchMapping("/items/{productId}/decrement")
    public ResponseEntity<CartItemResponseDto> decrementProductFromCart(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long productId, @Valid @RequestBody UpdateQuantityDto request){
        CartItem cartItem = cartService.itemOnCartDecrement(principal.getId(), productId, request.getQuantity());
        return ResponseEntity.status(HttpStatus.OK).body(toCartItemResponseDto(cartItem));
    }

    private CartItemResponseDto toCartItemResponseDto(CartItem cartItem){
        return new CartItemResponseDto(cartItem.getProduct().getId(), cartItem.getProduct().getName(), cartItem.getQuantity(), cartItem.getPrice(), cartItem.calculateSubtotal());
    }

}
