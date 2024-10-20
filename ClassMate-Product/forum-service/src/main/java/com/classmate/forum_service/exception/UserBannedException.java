package com.classmate.forum_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a forbidden action is attempted, such as a banned user trying to subscribe to a forum.
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UserBannedException extends RuntimeException {

    /**
     * Constructs a new UserBannedException with the specified detail message.
     *
     * @param message the detail message
     */
    public UserBannedException(String message) {
        super(message);
    }
}
