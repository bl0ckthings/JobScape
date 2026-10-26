package com.jobscape.applicationservice.dto;

import com.jobscape.applicationservice.model.ApplicationStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        application.getSteps().add(step);

        assertEquals(7L, application.getId());
        assertEquals(11L, application.getUserId());
        assertEquals(13L, application.getCompanyId());
        assertEquals(7L, application.getSteps().get(0).getApplicationId());
    }

    @Test
    void createApplicationRequestCarriesCreationInputsAndAuthenticatedUser() {
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
        request.setUserId(11L);

        assertEquals("JobScape", request.getCompanyName());
        assertEquals("jobscape.com", request.getCompanyDomain());
        assertEquals(ApplicationStatus.APPLIED, request.getInitialStatus());
        assertEquals(LocalDate.of(2026, 10, 16), request.getInitialDate());
        assertEquals(11L, request.getUserId());
        assertTrue(request.isCreateReminder());
    }
}
