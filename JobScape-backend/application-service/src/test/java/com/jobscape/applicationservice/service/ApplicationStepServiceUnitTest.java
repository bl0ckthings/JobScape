package com.jobscape.applicationservice.service;

import com.jobscape.applicationservice.client.*;
import com.jobscape.applicationservice.dto.application.ApplicationDto;
import com.jobscape.applicationservice.dto.applicationstep.CreateApplicationStepRequest;
import com.jobscape.applicationservice.model.ApplicationStatus;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationStepServiceUnitTest {
    private final ApplicationStepClient steps = mock(ApplicationStepClient.class);
    private final ApplicationClient applications = mock(ApplicationClient.class);
    private final ContactClient contacts = mock(ContactClient.class);
    private final ApplicationStepService service = new ApplicationStepService(steps, applications, contacts);

    @Test
    void rejectsApplicationNotOwnedByUser() {
        ApplicationDto application = new ApplicationDto();
        application.setUserId(99L);
        when(applications.getApplicationById(42L)).thenReturn(application);

        var error = assertThrows(ResponseStatusException.class,
                () -> service.getByApplicationId(42L, 7L));

        assertEquals(404, error.getStatusCode().value());
        verifyNoInteractions(steps, contacts);
    }

    @Test
    void rejectsContactOutsideAuthorizedContacts() {
        ApplicationDto application = new ApplicationDto();
        application.setUserId(7L);
        application.setCompanyId(3L);
        when(applications.getApplicationById(42L)).thenReturn(application);
        when(contacts.getContactsByCompanyId(3L, 7L)).thenReturn(List.of());
        CreateApplicationStepRequest request = new CreateApplicationStepRequest();
        request.setStepStatus(ApplicationStatus.FIRST_CONTACT);
        request.setStepDate(LocalDateTime.of(2026, 11, 3, 10, 0));
        request.setContactId(12L);

        assertThrows(ResponseStatusException.class, () -> service.createStep(42L, 7L, request));
        verifyNoInteractions(steps);
    }

    @Test
    void rejectsMissingUserIdentityBeforeExternalCalls() {
        var error = assertThrows(ResponseStatusException.class,
                () -> service.getStepById(900L, null));

        assertEquals(404, error.getStatusCode().value());
        verifyNoInteractions(applications, steps, contacts);
    }
}
