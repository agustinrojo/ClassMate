package com.example.Security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TokenAlreadyConfirmedException extends RuntimeException{
    private String token;

    public TokenAlreadyConfirmedException(String token) {
        super(String.format("Token '%s' has already been confirmed.", token));
        this.token = token;
    }
}
