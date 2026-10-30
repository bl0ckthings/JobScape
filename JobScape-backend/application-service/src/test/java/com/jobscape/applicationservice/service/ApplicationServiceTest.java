package com.jobscape.applicationservice.service;

import com.jobscape.applicationservice.client.ApplicationClient;
import com.jobscape.applicationservice.client.ContactClient;
import com.jobscape.applicationservice.client.ApplicationStepClient;
import com.jobscape.applicationservice.client.CompanyClient;
import com.jobscape.applicationservice.client.ReminderClient;
import com.jobscape.applicationservice.dto.application.ApplicationDto;
import com.jobscape.applicationservice.dto.application.CreateApplicationRequest;
import com.jobscape.applicationservice.dto.applicationstep.ApplicationStepDto;
import com.jobscape.applicationservice.dto.company.CompanyDto;
import com.jobscape.applicationservice.dto.reminder.CreateReminderRequest;
import com.jobscape.applicationservice.model.ApplicationStatus;
import com.jobscape.applicationservice.service.mapper.ApplicationMapper;
import com.jobscape.applicationservice.service.mapper.ApplicationStepMapper;
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
        CompanyClient companyClient = mock(CompanyClient.class);
        CompanyDto company = new CompanyDto();
        company.setId(13L);
        when(companyClient.resolveCompany(any())).thenReturn(company);
        ApplicationService service = new ApplicationService(
                applicationClient,
                applicationStepClient,
                reminderClient,
                companyClient,
                mock(ContactClient.class),
                new ApplicationMapper(),
                new ApplicationStepMapper()
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
        createdApplication.setStatus(ApplicationStatus.APPLIED);
        when(applicationClient.create(any(ApplicationDto.class))).thenReturn(createdApplication);

        ApplicationDto result = service.create(request);

        ArgumentCaptor<ApplicationDto> applicationCaptor = ArgumentCaptor.forClass(ApplicationDto.class);
        ArgumentCaptor<ApplicationStepDto> stepCaptor = ArgumentCaptor.forClass(ApplicationStepDto.class);
        ArgumentCaptor<CreateReminderRequest> reminderCaptor = ArgumentCaptor.forClass(CreateReminderRequest.class);
        InOrder calls = inOrder(applicationClient, applicationStepClient, reminderClient);
        calls.verify(applicationClient).create(applicationCaptor.capture());
        calls.verify(applicationStepClient).create(stepCaptor.capture());
        calls.verify(reminderClient).create(reminderCaptor.capture());

        assertEquals(42L, result.getId());
        assertEquals("Backend Developer", applicationCaptor.getValue().getJobTitle());
        assertEquals(11L, applicationCaptor.getValue().getUserId());
        assertEquals(42L, stepCaptor.getValue().getApplicationId());
        assertEquals(ApplicationStatus.APPLIED, stepCaptor.getValue().getStepStatus());
        assertEquals(42L, reminderCaptor.getValue().getApplicationId());
        assertEquals(LocalDate.of(2026, 10, 19), reminderCaptor.getValue().getApplicationDate());
        assertEquals("Relancer l'entreprise", reminderCaptor.getValue().getComment());
    }

    @Test
    void usesCreatedApplicationDateForReminderWhenInitialDateIsMissing() {
        ApplicationClient applicationClient = mock(ApplicationClient.class);
        ApplicationStepClient applicationStepClient = mock(ApplicationStepClient.class);
        ReminderClient reminderClient = mock(ReminderClient.class);
        CompanyClient companyClient = mock(CompanyClient.class);
        CompanyDto company = new CompanyDto();
        company.setId(13L);
        when(companyClient.resolveCompany(any())).thenReturn(company);
        ApplicationService service = new ApplicationService(
                applicationClient,
                applicationStepClient,
                reminderClient,
                companyClient,
                mock(ContactClient.class),
                new ApplicationMapper(),
                new ApplicationStepMapper()
        );

        CreateApplicationRequest request = new CreateApplicationRequest();
        request.setInitialStatus(ApplicationStatus.APPLIED);
        request.setCreateReminder(true);

        ApplicationDto createdApplication = new ApplicationDto();
        createdApplication.setId(42L);
        createdApplication.setStatus(ApplicationStatus.APPLIED);
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
