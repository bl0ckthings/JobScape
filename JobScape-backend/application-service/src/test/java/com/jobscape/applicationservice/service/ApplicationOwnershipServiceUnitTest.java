package com.jobscape.applicationservice.service;

import com.jobscape.applicationservice.client.ApplicationClient;
import com.jobscape.applicationservice.dto.application.ApplicationDto;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ApplicationOwnershipServiceUnitTest {
    private final ApplicationClient applications = mock(ApplicationClient.class);
    private final ApplicationOwnershipService ownership = new ApplicationOwnershipService(applications);

    @Test
    void returnsApplicationOwnedByUser() {
        ApplicationDto application = new ApplicationDto();
        application.setId(42L);
        application.setUserId(7L);
        when(applications.getApplicationById(42L)).thenReturn(application);

        assertSame(application, ownership.requireOwnedApplication(42L, 7L));
    }

    @Test
    void hidesApplicationOwnedByAnotherUser() {
        ApplicationDto application = new ApplicationDto();
        application.setUserId(99L);
        when(applications.getApplicationById(42L)).thenReturn(application);

        var error = assertThrows(ResponseStatusException.class,
                () -> ownership.requireOwnedApplication(42L, 7L));

        assertEquals(404, error.getStatusCode().value());
    }

    @Test
    void rejectsMissingUserBeforeCallingEntityManager() {
        var error = assertThrows(ResponseStatusException.class,
                () -> ownership.requireOwnedApplication(42L, null));

        assertEquals(404, error.getStatusCode().value());
        verifyNoInteractions(applications);
    }
}
