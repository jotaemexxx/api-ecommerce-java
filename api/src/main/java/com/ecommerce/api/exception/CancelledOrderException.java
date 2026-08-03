package com.ecommerce.api.exception;

public class CancelledOrderException extends RuntimeException {
    public CancelledOrderException(String message) {
        super(message);
    }
}
