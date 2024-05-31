package com.classmate.comment_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a comment is not found.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends RuntimeException {

    /**
     * Constructs a new CommentNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public CommentNotFoundException(String message) {
        super(message);
    }
}
