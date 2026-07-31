package com.ecommerce.api.exception;

public class InvalidQuantityException extends RuntimeException {
    public InvalidQuantityException(String message){
        super(message);
    }
}
