package com.dsi.projspring.controllers;

import com.dsi.projspring.dtos.ErrorDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFound(EntityNotFoundException e, WebRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDTO> handleRuntime(RuntimeException e, WebRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

//    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
//    public ResponseEntity<ErrorDTO> handleAccessDenied(Exception e, WebRequest request) {
//        return buildResponse(HttpStatus.FORBIDDEN, "Access Denied: You don't have permission.", request);
//    }

    private ResponseEntity<ErrorDTO> buildResponse(HttpStatus status, String message, WebRequest request) {
        ErrorDTO error = ErrorDTO.builder().status(status.value()).message(message)
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false).replace("uri=",""))
                .build();
        return new ResponseEntity<>(error, status);
    }

//    @ExceptionHandler(EntityNotFoundException.class)
//    public ResponseEntity<Object> handleNotFound(EntityNotFoundException ex) {
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("message", ex.getMessage());
//        body.put("status", HttpStatus.NOT_FOUND.value());
//
//        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<Object> handleRuntime(RuntimeException ex) {
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("message", "An unexpected error occurred: " + ex.getMessage());
//        body.put("status", HttpStatus.BAD_REQUEST.value());
//
//        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
//    }
}
