package com.classmate.comment_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a comment is invalid.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidCommentException extends RuntimeException {

    /**
     * Constructs a new InvalidCommentException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidCommentException(String message) {
        super(message);
    }
}
