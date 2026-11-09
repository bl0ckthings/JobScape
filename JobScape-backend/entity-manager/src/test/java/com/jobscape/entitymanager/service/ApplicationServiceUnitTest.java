package com.jobscape.entitymanager.service;

import com.jobscape.entitymanager.dto.*;
import com.jobscape.entitymanager.model.*;
import com.jobscape.entitymanager.repository.*;
import com.jobscape.entitymanager.service.mapper.*;
import com.jobscape.entitymanager.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationServiceUnitTest {
    private final ApplicationRepository applications = mock(ApplicationRepository.class);
    private final UserRepository users = mock(UserRepository.class);
    private final CompanyRepository companies = mock(CompanyRepository.class);
    private final ApplicationService service = new ApplicationService(applications, companies, users,
            new ApplicationMapper(new ApplicationStepMapper()));

    @Test
    void updatesCityWithoutClearingJobTitle() {
        Application application = new Application();
        application.setJobTitle("Développeur Java");
        application.setCity("Paris");
        application.setUser(new User());
        application.setCompany(new Company());
        when(applications.findById(42L)).thenReturn(Optional.of(application));
        when(applications.save(application)).thenReturn(application);
        ApplicationDto update = new ApplicationDto();
        update.setCity("Marseille");

        var result = service.update(42L, update);

        assertEquals("Marseille", result.getCity());
        assertEquals("Développeur Java", result.getJobTitle());
    }

    @Test
    void rejectsMissingApplication() {
        when(applications.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test
    void doesNotCreateApplicationForMissingUser() {
        ApplicationDto request = new ApplicationDto();
        request.setUserId(99L);
        request.setCompanyId(3L);
        when(users.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.create(request));
        verify(applications, never()).save(any());
    }
}
