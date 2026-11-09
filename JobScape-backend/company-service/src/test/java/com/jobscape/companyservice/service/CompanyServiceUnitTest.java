package com.jobscape.companyservice.service;

import com.jobscape.companyservice.client.CompanyClient;
import com.jobscape.companyservice.dto.CompanyDto;
import com.jobscape.companyservice.dto.CompanySelectionDto;
import com.jobscape.companyservice.service.mapper.CompanyMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompanyServiceUnitTest {
    private final CompanyClient client = mock(CompanyClient.class);
    private final CompanyService service = new CompanyService(client, new CompanyMapper());

    @Test
    void findsCompanyAfterDomainNormalization() {
        CompanySelectionDto selection = new CompanySelectionDto();
        selection.setName("Acme");
        selection.setDomain(" https://WWW.ACME.COM./jobs ");
        CompanyDto existing = new CompanyDto();
        existing.setId(3L);
        when(client.getCompanyByDomain("acme.com")).thenReturn(existing);

        assertSame(existing, service.resolveCompany(selection));
        verify(client, never()).createCompany(any());
    }

    @Test
    void createsCompanyWithoutDomain() {
        CompanySelectionDto selection = new CompanySelectionDto();
        selection.setName("Acme");
        when(client.createCompany(any())).thenAnswer(call -> call.getArgument(0));

        var result = service.resolveCompany(selection);

        assertEquals("Acme", result.getName());
        assertNull(result.getDomain());
        verify(client, never()).getCompanyByDomain(any());
    }

    @Test
    void rejectsSelectionWithoutName() {
        CompanySelectionDto selection = new CompanySelectionDto();
        selection.setName(" ");

        assertThrows(IllegalArgumentException.class, () -> service.resolveCompany(selection));
        verifyNoInteractions(client);
    }
}
