package com.ecommerce.api.exception;

public class RemoveInvalidCartItemException extends RuntimeException {
    public RemoveInvalidCartItemException(String message) {
        super(message);
    }
}
