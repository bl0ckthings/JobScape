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

class CompanyServiceUnitTest {
    private final CompanyRepository companies = mock(CompanyRepository.class);
    private final CompanyService service = new CompanyService(companies, new CompanyMapper());

    @Test
    void updatesWebsiteWithoutClearingName() {
        Company company = new Company();
        company.setName("Acme");
        company.setWebsite("https://ancien.test");
        when(companies.findById(3L)).thenReturn(Optional.of(company));
        when(companies.save(company)).thenReturn(company);
        CompanyDto update = new CompanyDto();
        update.setWebsite("https://nouveau.test");

        var result = service.update(3L, update);

        assertEquals("Acme", result.getName());
        assertEquals("https://nouveau.test", result.getWebsite());
    }

    @Test
    void rejectsMissingCompany() {
        when(companies.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }
}
