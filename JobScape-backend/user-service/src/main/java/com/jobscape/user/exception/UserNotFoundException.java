package com.jobscape.user.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException  extends ApiError {
    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "User not found" );
    }
}
