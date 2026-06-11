package com.jobscape.user.controller;

import com.jobscape.user.exception.ApiError;
import com.jobscape.user.exception.ApiErrorBody;
import com.jobscape.user.exception.UserEmailAlreadyExistException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiError.class)
    public ResponseEntity<ApiErrorBody> handleApiError(ApiError error) {
        return error.toResponse();
    }
}
