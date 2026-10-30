package com.jobscape.reminderservice.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityManagerError(FeignException exception) {
        boolean notFound = exception.status() == 404;
        HttpStatus status = notFound ? HttpStatus.NOT_FOUND : HttpStatus.BAD_GATEWAY;
        ApiErrorResponse body = new ApiErrorResponse(status.value(),
                notFound ? "RESOURCE_NOT_FOUND" : "UPSTREAM_ERROR",
                notFound ? "Reminder not found" : "Entity manager request failed", Instant.now());
        return ResponseEntity.status(status).body(body);
    }
}
