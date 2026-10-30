package com.jobscape.reminderservice.exception;

import java.time.Instant;

public record ApiErrorResponse(int status, String code, String message, Instant timestamp) {
}
