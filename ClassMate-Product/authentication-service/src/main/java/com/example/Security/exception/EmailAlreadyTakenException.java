package com.example.Security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class EmailAlreadyTakenException extends RuntimeException{
    private String email;

    public EmailAlreadyTakenException(String email) {
        super(String.format("Email '%s' is already taken.", email));
        this.email = email;
    }
}
