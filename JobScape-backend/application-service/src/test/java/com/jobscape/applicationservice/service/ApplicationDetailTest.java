package com.jobscape.applicationservice.service;

import com.jobscape.applicationservice.client.*;
import com.jobscape.applicationservice.dto.application.ApplicationDto;
import com.jobscape.applicationservice.dto.contact.ContactSummaryDto;
import com.jobscape.applicationservice.dto.reminder.ReminderDto;
import com.jobscape.applicationservice.service.mapper.ApplicationMapper;
import com.jobscape.applicationservice.service.mapper.ApplicationStepMapper;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationDetailTest {
    private final ApplicationClient applications = mock(ApplicationClient.class);
    private final ContactClient contacts = mock(ContactClient.class);
    private final ReminderClient reminders = mock(ReminderClient.class);
    private final ApplicationService service = new ApplicationService(applications,
            mock(ApplicationStepClient.class), reminders, mock(CompanyClient.class), contacts,
            new ApplicationMapper(), new ApplicationStepMapper());

    @Test void assemblesDetailsWithContactsScopedToCompanyAndOwner() {
        ApplicationDto application = new ApplicationDto();
        application.setId(12L);
        application.setUserId(7L);
        application.setCompanyId(3L);
        List<ContactSummaryDto> companyContacts = List.of(new ContactSummaryDto());
        List<ReminderDto> applicationReminders = List.of(new ReminderDto());
        when(applications.getApplicationById(12L)).thenReturn(application);
        when(contacts.getContactsByCompanyId(3L, 7L)).thenReturn(companyContacts);
        when(reminders.getRemindersByApplicationId(12L)).thenReturn(applicationReminders);
        var detail = service.getApplicationDetail(12L, 7L);
        assertSame(application, detail.getApplication());
        assertEquals(companyContacts, detail.getContacts());
        assertEquals(applicationReminders, detail.getReminders());
        verify(contacts, never()).getContactsByUserId(anyLong());
    }

    @Test void refusesAnotherOwnerBeforeCallingOtherServices() {
        ApplicationDto application = new ApplicationDto();
        application.setUserId(99L);
        when(applications.getApplicationById(12L)).thenReturn(application);
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.getApplicationDetail(12L, 7L)).getStatusCode().value());
        verifyNoInteractions(contacts, reminders);
    }

    @Test void missingIdentityCannotAuthorizeAnApplicationWithoutOwner() {
        when(applications.getApplicationById(12L)).thenReturn(new ApplicationDto());
        assertThrows(ResponseStatusException.class, () -> service.getApplicationDetail(12L, null));
        verifyNoInteractions(contacts, reminders);
    }
}
