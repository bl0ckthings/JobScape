package com.jobscape.entitymanager.service;

import com.jobscape.entitymanager.dto.*;
import com.jobscape.entitymanager.model.*;
import com.jobscape.entitymanager.repository.*;
import com.jobscape.entitymanager.service.mapper.*;
import com.jobscape.entitymanager.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationStepServiceUnitTest {
    private final ApplicationStepRepository steps = mock(ApplicationStepRepository.class);
    private final ApplicationRepository applications = mock(ApplicationRepository.class);
    private final ContactRepository contacts = mock(ContactRepository.class);
    private final ApplicationStepService service = new ApplicationStepService(steps, applications,
            contacts, new ApplicationStepMapper());

    @Test
    void updatesCommentWithoutChangingApplication() {
        Application application = new Application();
        application.setId(42L);
        ApplicationStep step = new ApplicationStep();
        step.setApplication(application);
        step.setStepStatus(ApplicationStatus.APPLIED);
        when(steps.findById(900L)).thenReturn(Optional.of(step));
        when(steps.save(step)).thenReturn(step);
        ApplicationStepDto update = new ApplicationStepDto();
        update.setComment("Entretien confirmé");

        var result = service.update(900L, update);

        assertEquals(42L, result.getApplicationId());
        assertEquals(ApplicationStatus.APPLIED, result.getStepStatus());
        assertEquals("Entretien confirmé", result.getComment());
    }

    @Test
    void rejectsMissingStep() {
        when(steps.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test
    void rejectsCreationWithoutApplication() {
        assertThrows(IllegalArgumentException.class, () -> service.create(new ApplicationStepDto()));
        verifyNoInteractions(steps);
    }

    @Test
    void clearsContactWhenUpdatedContactIdIsNull() {
        ApplicationStep step = new ApplicationStep();
        step.setContact(new Contact());
        step.setApplication(new Application());
        when(steps.findById(900L)).thenReturn(Optional.of(step));
        when(steps.save(step)).thenReturn(step);

        service.update(900L, new ApplicationStepDto());

        assertNull(step.getContact());
        verifyNoInteractions(contacts);
    }
}
