package com.jobscape.companyservice.exception;

import java.time.Instant;

public record ApiErrorResponse(
        int status,
        String code,
        String message,
        Instant timestamp
) {
}
