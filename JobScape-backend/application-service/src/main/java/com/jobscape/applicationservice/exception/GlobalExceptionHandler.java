package com.jobscape.applicationservice.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiErrorResponse> handleUpstreamError(FeignException exception) {
        boolean notFound = exception.status() == 404;
        HttpStatus status = notFound ? HttpStatus.NOT_FOUND : HttpStatus.BAD_GATEWAY;
        return ResponseEntity.status(status).body(new ApiErrorResponse(status.value(),
                notFound ? "RESOURCE_NOT_FOUND" : "UPSTREAM_ERROR",
                notFound ? "Resource not found" : "Upstream service request failed", Instant.now()));
    }
}
