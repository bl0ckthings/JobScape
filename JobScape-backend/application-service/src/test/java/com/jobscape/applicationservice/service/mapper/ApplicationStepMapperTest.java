package com.jobscape.applicationservice.service.mapper;

import com.jobscape.applicationservice.dto.application.ApplicationDto;
import com.jobscape.applicationservice.dto.application.CreateApplicationRequest;
import com.jobscape.applicationservice.dto.applicationstep.ApplicationStepDto;
import com.jobscape.applicationservice.model.ApplicationStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ApplicationStepMapperTest {

    private final ApplicationStepMapper mapper = new ApplicationStepMapper();

    @Test
    void mapsInitialStepUsingSavedApplicationAsReference() {
        CreateApplicationRequest request = new CreateApplicationRequest();
        request.setComment("Application sent");
        request.setInitialDate(LocalDate.of(2026, 10, 19));

        ApplicationDto application = new ApplicationDto();
        application.setId(42L);
        application.setStatus(ApplicationStatus.APPLIED);

        ApplicationStepDto step = mapper.toInitialDto(request, application);

        assertEquals("Application sent", step.getComment());
        assertEquals(LocalDate.of(2026, 10, 19).atStartOfDay(), step.getStepDate());
        assertEquals(ApplicationStatus.APPLIED, step.getStepStatus());
        assertEquals(42L, step.getApplicationId());
        assertNull(step.getId());
        assertNull(step.getContactId());
        assertNull(step.getCreatedAt());
        assertNull(step.getUpdatedAt());
    }

    @Test
    void leavesStepDateUnsetWhenInitialDateIsMissing() {
        ApplicationStepDto step = mapper.toInitialDto(new CreateApplicationRequest(), new ApplicationDto());

        assertNull(step.getStepDate());
    }
}
