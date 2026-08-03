package com.ecommerce.api.exception;

public class PaidOrderException extends RuntimeException {
    public PaidOrderException(String message) {
        super(message);
    }
}
