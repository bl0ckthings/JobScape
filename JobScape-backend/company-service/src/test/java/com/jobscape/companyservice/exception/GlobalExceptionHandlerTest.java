package com.jobscape.companyservice.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    @Test
    void returnsStructuredInvalidCompanyResponse() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ApiErrorResponse> response = handler.handleInvalidCompany(
                new IllegalArgumentException("Company name is required")
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().status());
        assertEquals("INVALID_COMPANY", response.getBody().code());
        assertEquals("Company name is required", response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }
}
