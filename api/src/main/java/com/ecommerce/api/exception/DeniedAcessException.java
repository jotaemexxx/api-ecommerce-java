package com.ecommerce.api.exception;

public class DeniedAcessException extends RuntimeException {
    public DeniedAcessException(String message) {
        super(message);
    }
}
