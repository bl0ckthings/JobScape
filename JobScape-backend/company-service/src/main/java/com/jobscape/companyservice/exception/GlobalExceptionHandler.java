package com.jobscape.companyservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCompany(IllegalArgumentException exception) {
        ApiErrorResponse body = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_COMPANY",
                exception.getMessage(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrorResponse> handleContactError(ResponseStatusException exception) {
        String code = exception.getStatusCode().value() == 404 ? "CONTACT_NOT_FOUND" : "INVALID_CONTACT";
        ApiErrorResponse body = new ApiErrorResponse(
                exception.getStatusCode().value(), code, exception.getReason(), Instant.now());
        return ResponseEntity.status(exception.getStatusCode()).body(body);
    }
}
