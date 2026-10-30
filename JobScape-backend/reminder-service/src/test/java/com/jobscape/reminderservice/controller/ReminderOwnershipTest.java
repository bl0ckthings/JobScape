package com.jobscape.reminderservice.controller;

import com.jobscape.reminderservice.client.ReminderClient;
import com.jobscape.reminderservice.service.ReminderService;
import com.jobscape.reminderservice.dto.ReminderDto;
import com.jobscape.reminderservice.exception.GlobalExceptionHandler;
import feign.FeignException;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReminderOwnershipTest {
    private final ReminderClient client = mock(ReminderClient.class);
    private final MockMvc mvc = MockMvcBuilders.standaloneSetup(new ReminderController(new ReminderService(client)))
            .setControllerAdvice(new GlobalExceptionHandler()).build();

    @Test void updateRequiresAuthenticatedIdentityFromCaller() throws Exception {
        mvc.perform(put("/reminder/update/12").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment\":\"Updated\"}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(client);
    }

    @Test void deleteRequiresAuthenticatedIdentityFromCaller() throws Exception {
        mvc.perform(delete("/reminder/delete/12")).andExpect(status().isBadRequest());
        verifyNoInteractions(client);
    }
    @Test void forwardsIdentityForUpdateAndDelete() throws Exception {
        when(client.updateReminder(eq(12L), eq(7L), any())).thenReturn(new ReminderDto());
        mvc.perform(put("/reminder/update/12").param("userId", "7")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"comment\":\"Updated\"}"))
                .andExpect(status().isOk());
        verify(client).updateReminder(eq(12L), eq(7L), argThat(body -> "Updated".equals(body.getComment())));
        mvc.perform(delete("/reminder/delete/12").param("userId", "7")).andExpect(status().isOk());
        verify(client).deleteReminder(12L, 7L);
    }

    @Test void preservesNotFoundForUnknownOrUnownedReminders() throws Exception {
        FeignException exception = notFound();
        when(client.updateReminder(eq(12L), eq(7L), any())).thenThrow(exception);
        doThrow(exception).when(client).deleteReminder(12L, 7L);
        mvc.perform(put("/reminder/update/12").param("userId", "7")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"comment\":\"Updated\"}"))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"));
        mvc.perform(delete("/reminder/delete/12").param("userId", "7"))
                .andExpect(status().isNotFound());
    }

    private FeignException notFound() {
        FeignException exception = mock(FeignException.class);
        when(exception.status()).thenReturn(404);
        return exception;
    }
}
