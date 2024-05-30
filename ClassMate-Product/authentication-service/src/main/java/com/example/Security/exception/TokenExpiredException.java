package com.example.Security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TokenExpiredException extends RuntimeException{
    private String token;

    public TokenExpiredException(String token) {
        super(String.format("Token '%s' has already expired.", token));
        this.token = token;
    }
}
