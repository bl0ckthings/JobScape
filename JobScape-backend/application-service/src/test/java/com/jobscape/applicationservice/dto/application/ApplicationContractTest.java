package com.jobscape.applicationservice.dto.application;

import com.jobscape.applicationservice.dto.applicationstep.ApplicationStepDto;
import com.jobscape.applicationservice.model.ApplicationStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationContractTest {

    @Test
    void applicationDtoCarriesApplicationStepContract() {
        ApplicationStepDto step = new ApplicationStepDto();
        step.setId(3L);
        step.setApplicationId(7L);
        step.setStepStatus(ApplicationStatus.APPLIED);
        step.setStepDate(LocalDateTime.of(2026, 10, 16, 9, 30));

        ApplicationDto application = new ApplicationDto();
        application.setId(7L);
        application.setJobTitle("Backend Developer");
        application.setUserId(11L);
        application.setCompanyId(13L);
        application.setCompanyName("Acme");
        application.setCompanyDomain("acme.com");
        application.setCompanyLogoUrl("https://example.com/acme.png");
        application.getSteps().add(step);

        assertEquals(7L, application.getId());
        assertEquals(11L, application.getUserId());
        assertEquals(13L, application.getCompanyId());
        assertEquals("Acme", application.getCompanyName());
        assertEquals("acme.com", application.getCompanyDomain());
        assertEquals("https://example.com/acme.png", application.getCompanyLogoUrl());
        assertEquals(7L, application.getSteps().get(0).getApplicationId());
    }

    @Test
    void createApplicationRequestCarriesCreationInputsAndAuthenticatedUser() {
        CreateApplicationRequest request = new CreateApplicationRequest();
        request.setCompanyName("JobScape");
        request.setCompanyDomain("jobscape.com");
        request.setCompanyLogoUrl("https://example.com/logo.png");
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
        request.setUserId(11L);

        assertEquals("JobScape", request.getCompanyName());
        assertEquals("jobscape.com", request.getCompanyDomain());
        assertEquals("https://example.com/logo.png", request.getCompanyLogoUrl());
        assertEquals(ApplicationStatus.APPLIED, request.getInitialStatus());
        assertEquals(LocalDate.of(2026, 10, 16), request.getInitialDate());
        assertEquals(11L, request.getUserId());
        assertTrue(request.isCreateReminder());
        assertNull(new CreateApplicationRequest().getCompanyLogoUrl());
    }
}
