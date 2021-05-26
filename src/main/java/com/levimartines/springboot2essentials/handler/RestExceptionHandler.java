package com.levimartines.springboot2essentials.handler;

import com.levimartines.springboot2essentials.exception.ResourceNotFoundException;
import com.levimartines.springboot2essentials.exception.StandardError;
import com.levimartines.springboot2essentials.exception.ValidationError;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFoundHandler(
        ResourceNotFoundException exception, HttpServletRequest request) {
        StandardError error = new StandardError(exception.getMessage(),
            HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException exception,
        HttpServletRequest request) {

        ValidationError error = new ValidationError("Validation error",
            HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());

        exception.getBindingResult().getFieldErrors()
            .forEach(err -> error.addError(err.getField(), err.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> defaultHandler(
        Exception exception, HttpServletRequest request) {
        StandardError error = new StandardError("Internal error",
            HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
