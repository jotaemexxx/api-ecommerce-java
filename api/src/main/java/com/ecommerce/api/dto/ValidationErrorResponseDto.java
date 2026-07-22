package com.ecommerce.api.dto;

import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Map;

public class ValidationErrorResponseDto {
    private int status;
    private Map<String, String> errors;
    private LocalDateTime timestamp;

    public ValidationErrorResponseDto(int status, Map<String, String> errors , LocalDateTime timestamp) {
        this.status = status;
        this.errors = errors;
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
