package com.jobscape.user.controller;

import com.jobscape.user.exception.ApiError;
import com.jobscape.user.exception.ApiErrorBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiError.class)
    public ResponseEntity<ApiErrorBody> handleApiError(ApiError error) {
        return error.toResponse();
    }
}
