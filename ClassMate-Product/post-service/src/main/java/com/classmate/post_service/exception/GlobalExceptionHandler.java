package com.classmate.post_service.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Global exception handler for handling all exceptions in the application.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles InvalidPostException and returns a BAD_REQUEST response.
     *
     * @param exception the exception
     * @param webRequest the web request
     * @return the response entity containing error details
     */
    @ExceptionHandler(InvalidPostException.class)
    public ResponseEntity<ErrorDetails> handleInvalidPostException(InvalidPostException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                "INVALID_POST"
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles PostNotFoundException and returns a NOT_FOUND response.
     *
     * @param exception the exception
     * @param webRequest the web request
     * @return the response entity containing error details
     */
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorDetails> handlePostNotFoundException(PostNotFoundException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                "POST_NOT_FOUND"
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleUserNotFoundException(UserNotFoundException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                "USER_NOT_FOUND"
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles UnauthorizedActionException and returns an UNAUTHORIZED response.
     *
     * @param exception the exception
     * @param webRequest the web request
     * @return the response entity containing error details
     */
    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<ErrorDetails> handleUnauthorizedActionException(UnauthorizedActionException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                "UNAUTHORIZED"
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles all other exceptions and returns an INTERNAL_SERVER_ERROR response.
     *
     * @param exception the exception
     * @param webRequest the web request
     * @return the response entity containing error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                "INTERNAL_SERVER_ERROR"
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles validation errors and returns a BAD_REQUEST response.
     * This method is useful for using @Valid annotations in request DTOs.
     *
     * @param exception the exception
     * @param headers the HTTP headers
     * @param status the HTTP status
     * @param webRequest the web request
     * @return the response entity containing validation error details
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest webRequest) {
        Map<String, String> errors = new HashMap<>();
        List<ObjectError> errorList = exception.getBindingResult().getAllErrors();

        errorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
