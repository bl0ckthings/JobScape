package com.jobscape.reminderservice.controller;

import com.jobscape.reminderservice.model.Notification;
import com.jobscape.reminderservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @MockitoBean
    private NotificationService notificationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createNotification() throws Exception {
        when(notificationService.createNotification(any(Notification.class))).thenAnswer(call -> {
            Notification notification = call.getArgument(0);
            notification.setId("32");
            return notification;
        });

        mockMvc.perform(post("/notifications/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "reminderId": 11,
                                  "applicationId": 5,
                                  "type": "REMINDER_DUE",
                                  "message": "Pense à rappeler {companyName}"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("32"))
                .andExpect(jsonPath("$.reminderId").value(11))
                .andExpect(jsonPath("$.applicationId").value(5))
                .andExpect(jsonPath("$.type").value("REMINDER_DUE"))
                .andExpect(jsonPath("$.message").value("Pense à rappeler {companyName}"));
    }
}
