package com.classmate.forum_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a forum is not found.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ForumNotFoundException extends RuntimeException {

    /**
     * Constructs a new ForumNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public ForumNotFoundException(String message) {
        super(message);
    }
}
