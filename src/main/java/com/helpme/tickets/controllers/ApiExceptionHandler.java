package com.helpme.tickets.controllers;

import com.helpme.tickets.exceptions.CategoryAlreadyExistsException;
import com.helpme.tickets.exceptions.CategoryNotFoundException;
import com.helpme.tickets.exceptions.TicketNotFoundException;
import com.helpme.tickets.model.ServerError;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;


@ControllerAdvice
public class ApiExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServerError> handleUnexpected(Exception ex, HttpServletRequest request) {
        logger.error("Unexpected error occurred", ex);

        ServerError error = new ServerError(
                "Unexpected error occurred. Please try again later.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getMethod(),
                request.getRequestURI(),
                Instant.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ServerError> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        logger.warn("Access denied for {}{}", request.getMethod(), request.getRequestURI());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ServerError(
                        "You are not allowed to perform this operation.",
                        HttpStatus.FORBIDDEN.value(),
                        request.getMethod(),
                        request.getRequestURI(),
                        Instant.now()
                        ));
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ServerError> handleCategoryAlreadyExists(CategoryAlreadyExistsException ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ServerError(
                        ex.getMessage(),
                        HttpStatus.CONFLICT.value(),
                        request.getMethod(),
                        request.getRequestURI(),
                        Instant.now()
                ));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ServerError> handleCategoryNotFound(CategoryNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ServerError(
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.value(),
                        request.getMethod(),
                        request.getRequestURI(),
                        Instant.now()
                ));
    }

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ServerError> handleTicketNotFound(TicketNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ServerError(
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.value(),
                        request.getMethod(),
                        request.getRequestURI(),
                        Instant.now()
                ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ServerError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ServerError(
                        ex.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getMethod(),
                        request.getRequestURI(),
                        Instant.now()
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ServerError> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ServerError(
                        ex.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getMethod(),
                        request.getRequestURI(),
                        Instant.now()
                ));
    }
}
