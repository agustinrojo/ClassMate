package com.classmate.post_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a post is invalid.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidPostException extends RuntimeException {

    /**
     * Constructs a new InvalidPostException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidPostException(String message) {
        super(message);
    }
}
