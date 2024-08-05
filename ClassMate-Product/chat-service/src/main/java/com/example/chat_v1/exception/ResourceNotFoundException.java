package com.example.chat_v1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    private String resource;
    private String field;
    private String value;

    public ResourceNotFoundException(String resource, String field, String value) {
        super(String.format("%s with %s : %s not found.", resource, field, value));
        this.resource = resource;
        this.field = field;
        this.value = value;
    }
}
