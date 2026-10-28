package com.jobscape.companyservice.service;

import com.jobscape.companyservice.client.CompanyClient;
import com.jobscape.companyservice.dto.CompanyDto;
import com.jobscape.companyservice.dto.CompanySelectionDto;
import com.jobscape.companyservice.service.mapper.CompanyMapper;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CompanyServiceTest {

    private final CompanyClient client = mock(CompanyClient.class);
    private final CompanyService service = new CompanyService(client, new CompanyMapper());

    @Test
    void forwardsCompanyOperationsToEntityManager() {
        CompanyDto company = new CompanyDto();
        company.setName("New startup");
        company.setLogoUrl("https://example.com/logo.png");
        List<CompanyDto> companies = List.of(company);
        when(client.getCompanies()).thenReturn(companies);
        when(client.getCompanyById(7L)).thenReturn(company);
        when(client.createCompany(company)).thenReturn(company);

        assertEquals(companies, service.getCompanies());
        assertEquals(company, service.getCompanyById(7L));
        assertEquals(company, service.createCompany(company));
        assertEquals("https://example.com/logo.png", company.getLogoUrl());
        service.deleteCompany(7L);

        verify(client).getCompanies();
        verify(client).getCompanyById(7L);
        verify(client).createCompany(company);
        verify(client).deleteCompany(7L);
    }

    @Test
    void normalizesOptionalDomainWithoutChangingWebsite() {
        CompanyDto company = new CompanyDto();
        company.setDomain("https://www.Example.com/jobs");
        company.setWebsite("https://www.Example.com/jobs");
        when(client.createCompany(company)).thenReturn(company);

        service.createCompany(company);

        assertEquals("example.com", company.getDomain());
        assertEquals("https://www.Example.com/jobs", company.getWebsite());
        verify(client).createCompany(company);
    }

    @Test
    void findsCompanyByNormalizedDomain() {
        CompanyDto company = new CompanyDto();
        when(client.getCompanyByDomain("example.com")).thenReturn(company);

        assertEquals(Optional.of(company), service.findCompanyByDomain("https://www.Example.com/jobs"));
        verify(client).getCompanyByDomain("example.com");
    }

    @Test
    void returnsEmptyWhenDomainIsUnknown() {
        when(client.getCompanyByDomain("example.com"))
                .thenThrow(org.mockito.Mockito.mock(FeignException.NotFound.class));

        assertEquals(Optional.empty(), service.findCompanyByDomain("example.com"));
    }

    @Test
    void resolvesSelectionWithoutDomainByCreatingCompany() {
        CompanySelectionDto selection = selection("New startup", null);
        CompanyDto saved = new CompanyDto();
        saved.setId(7L);
        when(client.createCompany(any(CompanyDto.class))).thenReturn(saved);

        assertSame(saved, service.resolveCompany(selection));

        ArgumentCaptor<CompanyDto> created = ArgumentCaptor.forClass(CompanyDto.class);
        verify(client).createCompany(created.capture());
        assertEquals("New startup", created.getValue().getName());
        assertNull(created.getValue().getDomain());
        assertEquals("https://example.com/logo.png", created.getValue().getLogoUrl());
        verify(client, never()).getCompanyByDomain(anyString());
    }

    @Test
    void resolvesBlankDomainByCreatingCompanyWithoutLookup() {
        CompanySelectionDto selection = selection("New startup", "  ");
        CompanyDto saved = new CompanyDto();
        saved.setId(8L);
        when(client.createCompany(any(CompanyDto.class))).thenReturn(saved);

        assertSame(saved, service.resolveCompany(selection));

        ArgumentCaptor<CompanyDto> created = ArgumentCaptor.forClass(CompanyDto.class);
        verify(client).createCompany(created.capture());
        assertNull(created.getValue().getDomain());
        verify(client, never()).getCompanyByDomain(anyString());
    }

    @Test
    void resolvesExistingDomainWithoutCreatingCompany() {
        CompanySelectionDto selection = selection("Different name", "https://www.Example.com/jobs");
        CompanyDto existing = new CompanyDto();
        existing.setId(9L);
        existing.setName("Original name");
        when(client.getCompanyByDomain("example.com")).thenReturn(existing);

        assertSame(existing, service.resolveCompany(selection));
        verify(client).getCompanyByDomain("example.com");
        verify(client, never()).createCompany(any(CompanyDto.class));
    }

    @Test
    void resolvesUnknownDomainByCreatingCompany() {
        CompanySelectionDto selection = selection("Example", "https://www.Example.com/jobs");
        when(client.getCompanyByDomain("example.com"))
                .thenThrow(mock(FeignException.NotFound.class));
        CompanyDto saved = new CompanyDto();
        saved.setId(10L);
        when(client.createCompany(any(CompanyDto.class))).thenReturn(saved);

        assertSame(saved, service.resolveCompany(selection));

        ArgumentCaptor<CompanyDto> created = ArgumentCaptor.forClass(CompanyDto.class);
        verify(client).getCompanyByDomain("example.com");
        verify(client).createCompany(created.capture());
        assertEquals("Example", created.getValue().getName());
        assertEquals("example.com", created.getValue().getDomain());
        assertEquals("https://example.com/logo.png", created.getValue().getLogoUrl());
    }

    @Test
    void rejectsSelectionWithoutCompanyName() {
        CompanySelectionDto selection = selection("  ", "example.com");

        assertThrows(IllegalArgumentException.class, () -> service.resolveCompany(selection));
        verifyNoInteractions(client);
    }

    @Test
    void rejectsNullSelection() {
        assertThrows(IllegalArgumentException.class, () -> service.resolveCompany(null));
        verifyNoInteractions(client);
    }

    private CompanySelectionDto selection(String name, String domain) {
        CompanySelectionDto selection = new CompanySelectionDto();
        selection.setName(name);
        selection.setDomain(domain);
        selection.setLogoUrl("https://example.com/logo.png");
        return selection;
    }
}
