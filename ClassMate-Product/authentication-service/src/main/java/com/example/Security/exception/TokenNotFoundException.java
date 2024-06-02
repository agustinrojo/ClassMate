package com.example.Security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TokenNotFoundException extends RuntimeException{
    private String token;

    public TokenNotFoundException(String token) {
        super(String.format("Token '%s' not found.", token));
        this.token = token;
    }
}
