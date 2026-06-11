package com.jobscape.user.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiErrorBody {

        private String internal;
        private String message;
        private LocalDateTime timestamp;

        public ApiErrorBody(ApiError apiError) {
            this.internal = "Microservice Error";
            this.message = apiError.getMessage();
            this.timestamp = apiError.getTimestamp();
        }

}
