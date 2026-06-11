package com.jobscape.user.exception;

import lombok.*;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;



@Getter
@Setter
public class ApiError extends RuntimeException {

    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;

    public ApiError(HttpStatus status, String message ) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ResponseEntity<ApiErrorBody> toResponse() {
        return new ResponseEntity<>(new ApiErrorBody(
                this
        ), this.status );
    }


}


