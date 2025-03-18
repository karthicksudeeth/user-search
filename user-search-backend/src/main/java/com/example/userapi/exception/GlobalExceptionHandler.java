package com.example.userapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public Mono<String> handleUserNotFoundException(UserNotFoundException ex) {
        return Mono.just(ex.getMessage());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public Mono<String> handleResponseStatusException(ResponseStatusException ex) {
        return Mono.just("Error: " + ex.getReason());
    }

    @ExceptionHandler(Exception.class)
    public Mono<String> handleGenericException(Exception ex) {
        return Mono.just("An unexpected error occurred: " + ex.getMessage());
    }
}
