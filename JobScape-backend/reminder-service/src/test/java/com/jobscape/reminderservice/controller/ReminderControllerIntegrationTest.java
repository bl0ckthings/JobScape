package com.jobscape.reminderservice.controller;

import com.jobscape.reminderservice.client.ReminderClient;
import com.jobscape.reminderservice.dto.ReminderDto;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"spring.cloud.consul.enabled=false", "spring.cloud.consul.discovery.enabled=false"})
@AutoConfigureMockMvc
class ReminderControllerIntegrationTest {
    @Autowired private MockMvc mvc;
    @MockitoBean private ReminderClient client;

    @Test
    void createsAndRetrievesReminderCalculatedByRealService() throws Exception {
        when(client.createReminder(any())).thenAnswer(call -> {
            ReminderDto reminder = call.getArgument(0);
            reminder.setId(4L);
            return reminder;
        });
        mvc.perform(post("/reminder/create").contentType("application/json").content("""
                {"applicationId":12,"applicationDate":"2026-11-03","comment":"Relancer"}
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reminderDate").value("2026-11-10T09:00:00"));

        ReminderDto reminder = new ReminderDto();
        reminder.setId(4L);
        reminder.setReminderDate(LocalDateTime.of(2026, 11, 10, 9, 0));
        when(client.getRemindersByApplicationId(12L)).thenReturn(List.of(reminder));
        mvc.perform(get("/reminder/application/12")).andExpect(jsonPath("$[0].id").value(4));
    }

    @Test
    void rejectsIncompleteRequestBeforeExternalCalls() throws Exception {
        mvc.perform(post("/reminder/create").contentType("application/json").content("{}"))
                .andExpect(status().isBadRequest());
        mvc.perform(put("/reminder/update/4").contentType("application/json").content("{}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(client);
    }

    @Test
    void deletesReminderWithProvidedUserIdentity() throws Exception {
        mvc.perform(delete("/reminder/delete/4").param("userId", "7"))
                .andExpect(status().isOk());
        verify(client).deleteReminder(4L, 7L);
    }

    @Test
    void preservesRejectionForInaccessibleReminder() throws Exception {
        FeignException failure = mock(FeignException.class);
        when(failure.status()).thenReturn(404);
        doThrow(failure).when(client).deleteReminder(4L, 7L);

        mvc.perform(delete("/reminder/delete/4").param("userId", "7"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"));
    }
}
