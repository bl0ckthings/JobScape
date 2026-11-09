package com.jobscape.reminderservice.service;

import com.jobscape.reminderservice.client.ReminderClient;
import com.jobscape.reminderservice.dto.CreateReminderRequest;
import com.jobscape.reminderservice.dto.ReminderDto;
import com.jobscape.reminderservice.dto.UpdateReminderRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReminderServiceUnitTest {
    private final ReminderClient client = mock(ReminderClient.class);
    private final ReminderService service = new ReminderService(client);

    @Test
    void schedulesReminderSevenDaysLaterAtNine() {
        CreateReminderRequest request = new CreateReminderRequest();
        request.setApplicationId(12L);
        request.setApplicationDate(LocalDate.of(2026, 11, 3));
        request.setComment("Relancer");
        when(client.createReminder(any())).thenAnswer(call -> call.getArgument(0));

        var result = service.createReminder(request);

        assertEquals(LocalDateTime.of(2026, 11, 10, 9, 0), result.getReminderDate());
        assertEquals(12L, result.getApplicationId());
    }

    @Test
    void forwardsOwnerOnUpdate() {
        UpdateReminderRequest request = new UpdateReminderRequest();
        request.setComment("Nouvelle relance");
        ReminderDto response = new ReminderDto();
        when(client.updateReminder(12L, 7L, request)).thenReturn(response);

        assertSame(response, service.updateReminder(12L, 7L, request));
        verify(client).updateReminder(12L, 7L, request);
    }

    @Test
    void forwardsOwnerOnDelete() {
        service.deleteReminderById(12L, 7L);

        verify(client).deleteReminder(12L, 7L);
    }
}
