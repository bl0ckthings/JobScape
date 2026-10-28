package com.jobscape.webgateway.service.applicationDto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateApplicationRequestTest {

    @Test
    void carriesPublicApplicationCreationInputs() {
        CreateApplicationRequest request = new CreateApplicationRequest();
        request.setCompanyName("JobScape");
        request.setCompanyDomain("jobscape.com");
        request.setJobTitle("Backend Developer");
        request.setCity("Paris");
        request.setCountry("France");
        request.setSource("LinkedIn");
        request.setWorkMode("Remote");
        request.setUrl("https://example.com/jobs/1");
        request.setInitialStatus(ApplicationStatus.APPLIED);
        request.setInitialDate(LocalDate.of(2026, 10, 16));
        request.setComment("Applied through company website");
        request.setCreateReminder(true);

        assertEquals("JobScape", request.getCompanyName());
        assertEquals("jobscape.com", request.getCompanyDomain());
        assertEquals(ApplicationStatus.APPLIED, request.getInitialStatus());
        assertEquals(LocalDate.of(2026, 10, 16), request.getInitialDate());
        assertTrue(request.isCreateReminder());
    }
}
