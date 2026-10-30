package com.jobscape.applicationservice.exception;

import java.time.Instant;

public record ApiErrorResponse(int status, String code, String message, Instant timestamp) {
}
