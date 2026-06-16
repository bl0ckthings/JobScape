package com.jobscape.user.exception;

import org.springframework.http.HttpStatus;

public class UserEmailAlreadyExistException extends ApiError{

    public UserEmailAlreadyExistException(String email) {
        super(HttpStatus.CONFLICT, "An user with this email already exists: " + email);
    }
}
