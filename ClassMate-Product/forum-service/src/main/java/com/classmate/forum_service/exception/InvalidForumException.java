package com.classmate.forum_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a forum is invalid.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidForumException extends RuntimeException {

    /**
     * Constructs a new InvalidForumException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidForumException(String message) {
        super(message);
    }
}
