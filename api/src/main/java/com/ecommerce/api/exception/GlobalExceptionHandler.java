package com.ecommerce.api.exception;

import com.ecommerce.api.dto.ErrorResponseDto;
import com.ecommerce.api.dto.ValidationErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponseDto> handleResourceNotFound(ResourceNotFoundException ex) {
            ErrorResponseDto error = new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ValidationErrorResponseDto> handleValidationErrors(MethodArgumentNotValidException ex) {
            Map<String, String> errors = new HashMap<>();

            ex.getBindingResult().getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));

            ValidationErrorResponseDto reponse =  new ValidationErrorResponseDto(HttpStatus.BAD_REQUEST.value(), errors, LocalDateTime.now());

            return ResponseEntity.badRequest().body(reponse);
    }

}
