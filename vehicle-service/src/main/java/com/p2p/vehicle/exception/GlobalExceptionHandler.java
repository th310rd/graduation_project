package com.p2p.vehicle.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> nf(NotFoundException e) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage())); }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> br(BadRequestException e) { return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> mv(MethodArgumentNotValidException e) { return ResponseEntity.badRequest().body(Map.of("error", "Validation failed")); }
}
