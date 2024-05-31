package com.classmate.comment_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * A class representing the details of an error.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {

    /**
     * The timestamp when the error occurred.
     */
    private LocalDateTime timestamp;

    /**
     * The detail message of the error.
     */
    private String message;

    /**
     * The path where the error occurred.
     */
    private String path;

    /**
     * The error code representing the type of error.
     */
    private String errorCode;
}
