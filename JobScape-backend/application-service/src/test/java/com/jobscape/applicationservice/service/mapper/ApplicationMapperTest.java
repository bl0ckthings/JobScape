package com.jobscape.applicationservice.service.mapper;

import com.jobscape.applicationservice.dto.application.ApplicationDto;
import com.jobscape.applicationservice.dto.application.CreateApplicationRequest;
import com.jobscape.applicationservice.model.ApplicationStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationMapperTest {

    private final ApplicationMapper mapper = new ApplicationMapper();

    @Test
    void mapsCreationFieldsWithoutInventingServerOwnedFields() {
        CreateApplicationRequest request = new CreateApplicationRequest();
        request.setJobTitle("Backend Developer");
        request.setCity("Paris");
        request.setCountry("France");
        request.setSource("LinkedIn");
        request.setUrl("https://example.com/jobs/1");
        request.setInitialStatus(ApplicationStatus.APPLIED);
        request.setWorkMode("Remote");
        request.setUserId(11L);

        ApplicationDto application = mapper.toDto(request);

        assertEquals("Backend Developer", application.getJobTitle());
        assertEquals("Paris", application.getCity());
        assertEquals("France", application.getCountry());
        assertEquals("LinkedIn", application.getSource());
        assertEquals("https://example.com/jobs/1", application.getUrl());
        assertEquals(ApplicationStatus.APPLIED, application.getStatus());
        assertEquals("Remote", application.getWorkMode());
        assertEquals(11L, application.getUserId());
        assertNull(application.getId());
        assertNull(application.getCompanyId());
        assertNull(application.getCreatedAt());
        assertNull(application.getUpdatedAt());
        assertTrue(application.getSteps().isEmpty());
    }
}
