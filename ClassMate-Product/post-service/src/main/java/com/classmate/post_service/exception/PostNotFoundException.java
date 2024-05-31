package com.classmate.post_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a post is not found.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PostNotFoundException extends RuntimeException {

    /**
     * Constructs a new PostNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public PostNotFoundException(String message) {
        super(message);
    }
}
