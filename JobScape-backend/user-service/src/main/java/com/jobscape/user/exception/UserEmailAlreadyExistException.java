package com.jobscape.user.exception;

import org.springframework.http.HttpStatus;

public class UserEmailAlreadyExistException extends ApiError{

    public UserEmailAlreadyExistException() {
        super(HttpStatus.CONFLICT, "Email already exists");
    }
}
