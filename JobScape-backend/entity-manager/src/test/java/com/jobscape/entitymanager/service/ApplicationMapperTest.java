package com.jobscape.entitymanager.service;

import com.jobscape.entitymanager.dto.ApplicationDto;
import com.jobscape.entitymanager.model.Application;
import com.jobscape.entitymanager.model.ApplicationStep;
import com.jobscape.entitymanager.model.Company;
import com.jobscape.entitymanager.model.User;
import com.jobscape.entitymanager.service.mapper.ApplicationMapper;
import com.jobscape.entitymanager.service.mapper.ApplicationStepMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.jobscape.entitymanager.model.ApplicationStatus.APPLIED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ApplicationMapperTest {

    private final ApplicationMapper mapper = new ApplicationMapper(new ApplicationStepMapper());

    @Test
    void mapsApplicationSteps() {
        User user = new User();
        user.setId(1L);

        Company company = new Company();
        company.setId(2L);

        Application application = new Application();
        application.setUser(user);
        application.setCompany(company);

        ApplicationStep step = new ApplicationStep();
        step.setId(3L);
        step.setStepStatus(APPLIED);
        application.getSteps().add(step);

        ApplicationDto dto = mapper.toDto(application);

        assertEquals(1, dto.getSteps().size());
        assertEquals(3L, dto.getSteps().get(0).getId());
        assertEquals(APPLIED, dto.getSteps().get(0).getStepStatus());
    }

    @Test
    void mapsApplicationFieldsAndRelationshipsToDto() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 10, 14, 9, 0);
        LocalDateTime updatedAt = createdAt.plusHours(1);

        User user = new User();
        user.setId(1L);

        Company company = new Company();
        company.setId(2L);

        Application application = new Application();
        application.setId(3L);
        application.setJobTitle("Backend Developer");
        application.setCity("Marseille");
        application.setCountry("France");
        application.setSource("LinkedIn");
        application.setUrl("https://example.com/jobs/3");
        application.setStatus(APPLIED);
        application.setWorkMode("Hybrid");
        application.setCreatedAt(createdAt);
        application.setUpdatedAt(updatedAt);
        application.setUser(user);
        application.setCompany(company);

        ApplicationDto dto = mapper.toDto(application);

        assertEquals(3L, dto.getId());
        assertEquals("Backend Developer", dto.getJobTitle());
        assertEquals("Marseille", dto.getCity());
        assertEquals("France", dto.getCountry());
        assertEquals("LinkedIn", dto.getSource());
        assertEquals("https://example.com/jobs/3", dto.getUrl());
        assertEquals(APPLIED, dto.getStatus());
        assertEquals("Hybrid", dto.getWorkMode());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(updatedAt, dto.getUpdatedAt());
        assertEquals(1L, dto.getUserId());
        assertEquals(2L, dto.getCompanyId());
        assertEquals(0, dto.getSteps().size());
    }

    @Test
    void mapsApplicationDtoToEntityUsingResolvedRelationships() {
        ApplicationDto dto = new ApplicationDto();
        dto.setJobTitle("Backend Developer");
        dto.setCity("Marseille");
        dto.setCountry("France");
        dto.setSource("LinkedIn");
        dto.setUrl("https://example.com/jobs/3");
        dto.setStatus(APPLIED);
        dto.setWorkMode("Hybrid");

        User user = new User();
        Company company = new Company();

        Application application = mapper.toEntity(dto, user, company);

        assertEquals("Backend Developer", application.getJobTitle());
        assertEquals("Marseille", application.getCity());
        assertEquals("France", application.getCountry());
        assertEquals("LinkedIn", application.getSource());
        assertEquals("https://example.com/jobs/3", application.getUrl());
        assertEquals(APPLIED, application.getStatus());
        assertEquals("Hybrid", application.getWorkMode());
        assertSame(user, application.getUser());
        assertSame(company, application.getCompany());
    }
}
