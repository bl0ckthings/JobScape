package com.jobscape.reminderservice.service;

import com.jobscape.reminderservice.client.ReminderClient;
import com.jobscape.reminderservice.controller.ReminderController;
import com.jobscape.reminderservice.dto.ReminderDto;
import com.jobscape.reminderservice.dto.UpdateReminderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReminderUpdateContractTest {

    @Test
    void updateRequestOnlyCarriesMutableReminderFields() {
        Set<String> fields = Arrays.stream(UpdateReminderRequest.class.getDeclaredFields())
                .map(field -> field.getName())
                .collect(Collectors.toSet());

        assertEquals(Set.of("reminderDate", "comment"), fields);
    }

    @Test
    void clientUsesEntityManagerUpdateRouteWithReminderId() throws NoSuchMethodException {
        Method method = ReminderClient.class.getDeclaredMethod(
                "updateReminder",
                Long.class,
                UpdateReminderRequest.class
        );
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);

        assertArrayEquals(new String[]{"/internal/reminders/update/{id}"}, mapping.value());
        assertArrayEquals(new RequestMethod[]{RequestMethod.PUT}, mapping.method());
    }

    @Test
    void controllerExposesReminderIdOnUpdateRoute() throws NoSuchMethodException {
        Method method = ReminderController.class.getDeclaredMethod(
                "updateReminder",
                Long.class,
                UpdateReminderRequest.class
        );
        PutMapping mapping = method.getAnnotation(PutMapping.class);

        assertArrayEquals(new String[]{"/update/{id}"}, mapping.value());
    }

    @Test
    void serviceForwardsIdAndNarrowUpdateRequest() {
        ReminderClient client = mock(ReminderClient.class);
        ReminderService service = new ReminderService(client);
        UpdateReminderRequest request = new UpdateReminderRequest();
        ReminderDto expected = new ReminderDto();

        when(client.updateReminder(12L, request)).thenReturn(expected);

        ReminderDto result = service.updateReminder(12L, request);

        assertSame(expected, result);
        verify(client).updateReminder(12L, request);
    }
}
