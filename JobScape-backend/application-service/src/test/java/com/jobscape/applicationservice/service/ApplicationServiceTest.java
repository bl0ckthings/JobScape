package com.jobscape.applicationservice.service;

import com.jobscape.applicationservice.client.ApplicationClient;
import com.jobscape.applicationservice.client.ApplicationStepClient;
import com.jobscape.applicationservice.client.ReminderClient;
import com.jobscape.applicationservice.dto.ApplicationDto;
import com.jobscape.applicationservice.dto.ApplicationStepDto;
import com.jobscape.applicationservice.dto.CreateApplicationRequest;
import com.jobscape.applicationservice.dto.CreateReminderRequest;
import com.jobscape.applicationservice.model.ApplicationStatus;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApplicationServiceTest {

    @Test
    void createsApplicationBeforeStepAndReminderUsingGeneratedApplicationId() {
        ApplicationClient applicationClient = mock(ApplicationClient.class);
        ApplicationStepClient applicationStepClient = mock(ApplicationStepClient.class);
        ReminderClient reminderClient = mock(ReminderClient.class);
        ApplicationService service = new ApplicationService(
                applicationClient,
                applicationStepClient,
                reminderClient
        );

        CreateApplicationRequest request = new CreateApplicationRequest();
        request.setJobTitle("Backend Developer");
        request.setCity("Paris");
        request.setCountry("France");
        request.setSource("LinkedIn");
        request.setUrl("https://example.com/jobs/1");
        request.setInitialStatus(ApplicationStatus.APPLIED);
        request.setInitialDate(LocalDate.of(2026, 10, 19));
        request.setComment("Application sent");
        request.setWorkMode("Remote");
        request.setUserId(11L);
        request.setCreateReminder(true);

        ApplicationDto createdApplication = new ApplicationDto();
        createdApplication.setId(42L);
        when(applicationClient.create(any(ApplicationDto.class))).thenReturn(createdApplication);

        ApplicationDto result = service.create(request);

        ArgumentCaptor<ApplicationStepDto> stepCaptor = ArgumentCaptor.forClass(ApplicationStepDto.class);
        ArgumentCaptor<CreateReminderRequest> reminderCaptor = ArgumentCaptor.forClass(CreateReminderRequest.class);
        InOrder calls = inOrder(applicationClient, applicationStepClient, reminderClient);
        calls.verify(applicationClient).create(any(ApplicationDto.class));
        calls.verify(applicationStepClient).create(stepCaptor.capture());
        calls.verify(reminderClient).create(reminderCaptor.capture());

        assertEquals(42L, result.getId());
        assertEquals(42L, stepCaptor.getValue().getApplicationId());
        assertEquals(42L, reminderCaptor.getValue().getApplicationId());
        assertEquals(LocalDate.of(2026, 10, 19), reminderCaptor.getValue().getApplicationDate());
        assertEquals("Relancer l'entreprise", reminderCaptor.getValue().getComment());
    }

    @Test
    void usesCreatedApplicationDateForReminderWhenInitialDateIsMissing() {
        ApplicationClient applicationClient = mock(ApplicationClient.class);
        ApplicationStepClient applicationStepClient = mock(ApplicationStepClient.class);
        ReminderClient reminderClient = mock(ReminderClient.class);
        ApplicationService service = new ApplicationService(applicationClient, applicationStepClient, reminderClient);

        CreateApplicationRequest request = new CreateApplicationRequest();
        request.setInitialStatus(ApplicationStatus.APPLIED);
        request.setCreateReminder(true);

        ApplicationDto createdApplication = new ApplicationDto();
        createdApplication.setId(42L);
        createdApplication.setCreatedAt(LocalDateTime.of(2026, 10, 26, 15, 30));
        when(applicationClient.create(any(ApplicationDto.class))).thenReturn(createdApplication);

        service.create(request);

        ArgumentCaptor<CreateReminderRequest> reminderCaptor = ArgumentCaptor.forClass(CreateReminderRequest.class);
        InOrder calls = inOrder(applicationClient, applicationStepClient, reminderClient);
        calls.verify(applicationClient).create(any(ApplicationDto.class));
        calls.verify(applicationStepClient).create(any(ApplicationStepDto.class));
        calls.verify(reminderClient).create(reminderCaptor.capture());
        assertEquals(42L, reminderCaptor.getValue().getApplicationId());
        assertEquals(LocalDate.of(2026, 10, 26), reminderCaptor.getValue().getApplicationDate());
    }
}
