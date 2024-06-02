package com.example.Security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ResourceWithNumericValueDoesNotExistException extends RuntimeException{
    private String resource;
    private String field;
    private Long value;

    public ResourceWithNumericValueDoesNotExistException(String resource, String field, Long value) {
        super(String.format("%s not found with %s : %d", resource, field, value));
        this.resource = resource;
        this.field = field;
        this.value = value;
    }
}
