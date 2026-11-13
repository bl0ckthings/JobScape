package com.jobscape.applicationservice.service;

import com.jobscape.applicationservice.client.*;
import com.jobscape.applicationservice.dto.application.*;
import com.jobscape.applicationservice.dto.company.CompanyDto;
import com.jobscape.applicationservice.model.ApplicationCategory;
import com.jobscape.applicationservice.model.ApplicationStatus;
import com.jobscape.applicationservice.service.mapper.ApplicationMapper;
import com.jobscape.applicationservice.service.mapper.ApplicationStepMapper;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationServiceUnitTest {
    private final ApplicationClient applications = mock(ApplicationClient.class);
    private final ApplicationStepClient steps = mock(ApplicationStepClient.class);
    private final CompanyClient companies = mock(CompanyClient.class);
    private final ReminderClient reminders = mock(ReminderClient.class);
    private final ContactClient contacts = mock(ContactClient.class);
    private final ApplicationOwnershipService ownership = new ApplicationOwnershipService(applications);
    private final ApplicationService service = new ApplicationService(applications, steps, reminders,
            companies, contacts, new ApplicationMapper(), new ApplicationStepMapper(), ownership);

    @Test
    void createsCompanyBeforeApplicationAndFollowUp() {
        CompanyDto company = new CompanyDto();
        company.setId(3L);
        ApplicationDto created = new ApplicationDto();
        created.setId(42L);
        created.setStatus(ApplicationStatus.APPLIED);
        when(companies.resolveCompany(any())).thenReturn(company);
        when(applications.create(any())).thenReturn(created);
        CreateApplicationRequest request = new CreateApplicationRequest();
        request.setCompanyName("Acme");
        request.setUserId(7L);
        request.setInitialStatus(ApplicationStatus.APPLIED);
        request.setInitialDate(LocalDate.of(2026, 11, 3));
        request.setCreateReminder(true);

        assertSame(created, service.create(request));

        var calls = inOrder(companies, applications, steps, reminders);
        calls.verify(companies).resolveCompany(any());
        calls.verify(applications).create(argThat(dto -> Long.valueOf(3).equals(dto.getCompanyId())));
        calls.verify(steps).create(argThat(dto -> Long.valueOf(42).equals(dto.getApplicationId())));
        calls.verify(reminders).create(argThat(dto -> Long.valueOf(42).equals(dto.getApplicationId())));
    }

    @Test
    void rejectsAnotherUsersApplicationBeforeLoadingRelatedData() {
        ApplicationDto application = new ApplicationDto();
        application.setUserId(99L);
        when(applications.getApplicationById(42L)).thenReturn(application);

        var error = assertThrows(ResponseStatusException.class,
                () -> service.getApplicationDetail(42L, 7L));

        assertEquals(404, error.getStatusCode().value());
        verifyNoInteractions(companies, contacts, reminders);
    }

    @Test
    void groupsApplicationsWithoutLoadingTheSameCompanyTwice() {
        ApplicationDto sent = new ApplicationDto();
        sent.setCompanyId(3L);
        sent.setStatus(ApplicationStatus.APPLIED);
        ApplicationDto interview = new ApplicationDto();
        interview.setCompanyId(3L);
        interview.setStatus(ApplicationStatus.HR_INTERVIEW);
        CompanyDto company = new CompanyDto();
        company.setId(3L);
        when(applications.getApplicationByUserId(7L)).thenReturn(List.of(sent, interview));
        when(companies.getCompanyById(3L)).thenReturn(company);

        var board = service.getApplicationsByStatus(7L);

        assertEquals(List.of(sent), board.getApplicationsByStatus().get(ApplicationCategory.APPLIED));
        assertEquals(List.of(interview), board.getApplicationsByStatus().get(ApplicationCategory.INTERVIEW));
        verify(companies, times(1)).getCompanyById(3L);
    }

    @Test
    void updatesApplicationUsingIdFromDto() {
        ApplicationDto update = new ApplicationDto();
        update.setId(42L);
        ApplicationDto existing = new ApplicationDto();
        existing.setUserId(7L);
        when(applications.getApplicationById(42L)).thenReturn(existing);
        when(applications.update(update)).thenReturn(update);

        assertSame(update, service.updateApplication(update, 7L));

        verify(applications).update(update);
    }

    @Test
    void rejectsUpdateForAnotherUsersApplication() {
        ApplicationDto update = new ApplicationDto();
        update.setId(42L);
        ApplicationDto existing = new ApplicationDto();
        existing.setUserId(99L);
        when(applications.getApplicationById(42L)).thenReturn(existing);

        var error = assertThrows(ResponseStatusException.class,
                () -> service.updateApplication(update, 7L));

        assertEquals(404, error.getStatusCode().value());
        verify(applications, never()).update(any());
    }
}
