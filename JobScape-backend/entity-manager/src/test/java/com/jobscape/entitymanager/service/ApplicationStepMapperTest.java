package com.jobscape.entitymanager.service;

import com.jobscape.entitymanager.dto.ApplicationStepDto;
import com.jobscape.entitymanager.model.Application;
import com.jobscape.entitymanager.model.ApplicationStep;
import com.jobscape.entitymanager.model.Contact;
import com.jobscape.entitymanager.service.mapper.ApplicationStepMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.jobscape.entitymanager.model.ApplicationStatus.HR_INTERVIEW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class ApplicationStepMapperTest {

    private final ApplicationStepMapper mapper = new ApplicationStepMapper();

    @Test
    void mapsStepToDto() {
        LocalDateTime stepDate = LocalDateTime.of(2026, 10, 20, 14, 30);
        Contact contact = new Contact();
        contact.setId(7L);

        ApplicationStep step = new ApplicationStep();
        step.setId(1L);
        step.setComment("Phone interview");
        step.setStepDate(stepDate);
        step.setStepStatus(HR_INTERVIEW);
        step.setContact(contact);

        ApplicationStepDto dto = mapper.toDto(step);

        assertEquals(1L, dto.getId());
        assertEquals("Phone interview", dto.getComment());
        assertEquals(stepDate, dto.getStepDate());
        assertEquals(HR_INTERVIEW, dto.getStepStatus());
        assertEquals(7L, dto.getContactId());
    }

    @Test
    void mapsStepWithoutOptionalContact() {
        ApplicationStepDto dto = mapper.toDto(new ApplicationStep());

        assertNull(dto.getContactId());
    }

    @Test
    void mapsStepDtoToEntityUsingResolvedRelationships() {
        ApplicationStepDto dto = new ApplicationStepDto();
        dto.setComment("Phone interview");
        dto.setStepDate(LocalDateTime.of(2026, 10, 20, 14, 30));
        dto.setStepStatus(HR_INTERVIEW);

        Application application = new Application();
        application.setId(8L);
        Contact contact = new Contact();

        ApplicationStep step = mapper.toEntity(dto, application, contact);

        assertEquals(dto.getComment(), step.getComment());
        assertEquals(dto.getStepDate(), step.getStepDate());
        assertEquals(dto.getStepStatus(), step.getStepStatus());
        assertSame(application, step.getApplication());
        assertSame(contact, step.getContact());
    }

    @Test
    void mapsApplicationRelationshipToDto() {
        Application application = new Application();
        application.setId(8L);
        ApplicationStep step = new ApplicationStep();
        step.setApplication(application);

        ApplicationStepDto dto = mapper.toDto(step);

        assertEquals(8L, dto.getApplicationId());
    }
}
