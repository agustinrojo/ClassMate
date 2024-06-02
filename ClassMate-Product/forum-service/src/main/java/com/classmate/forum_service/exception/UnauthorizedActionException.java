package com.classmate.forum_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an unauthorized action is attempted.
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedActionException extends RuntimeException {

    /**
     * Constructs a new UnauthorizedActionException with the specified detail message.
     *
     * @param message the detail message
     */
    public UnauthorizedActionException(String message) {
        super(message);
    }
}
