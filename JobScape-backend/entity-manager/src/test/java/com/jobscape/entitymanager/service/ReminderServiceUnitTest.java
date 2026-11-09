package com.jobscape.entitymanager.service;

import com.jobscape.entitymanager.dto.*;
import com.jobscape.entitymanager.model.*;
import com.jobscape.entitymanager.repository.*;
import com.jobscape.entitymanager.service.mapper.*;
import com.jobscape.entitymanager.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReminderServiceUnitTest {
    private final ReminderRepository reminders = mock(ReminderRepository.class);
    private final ApplicationRepository applications = mock(ApplicationRepository.class);
    private final ReminderService service = new ReminderService(reminders, applications, new ReminderMapper());

    @Test
    void createsReminderLinkedToApplication() {
        Application application = new Application();
        application.setId(42L);
        when(applications.findById(42L)).thenReturn(Optional.of(application));
        when(reminders.save(any())).thenAnswer(call -> call.getArgument(0));
        ReminderDto request = new ReminderDto();
        request.setApplicationId(42L);
        request.setReminderDate(LocalDateTime.of(2026, 11, 10, 9, 0));

        assertEquals(42L, service.create(request).getApplicationId());
    }

    @Test
    void rejectsUpdateWhenReminderIsNotOwnedByUser() {
        when(reminders.updateOwnedReminder(eq(4L), eq(7L), any(), any(), any())).thenReturn(0);

        assertThrows(ResourceNotFoundException.class,
                () -> service.update(4L, 7L, new ReminderDto()));
    }

    @Test
    void rejectsCreationWithoutApplication() {
        assertThrows(IllegalArgumentException.class, () -> service.create(new ReminderDto()));
        verifyNoInteractions(reminders);
    }
}
