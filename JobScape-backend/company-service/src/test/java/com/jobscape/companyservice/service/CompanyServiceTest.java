package com.jobscape.companyservice.service;

import com.jobscape.companyservice.client.CompanyClient;
import com.jobscape.companyservice.dto.CompanyDto;
import feign.FeignException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CompanyServiceTest {

    private final CompanyClient client = mock(CompanyClient.class);
    private final CompanyService service = new CompanyService(client);

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
}
