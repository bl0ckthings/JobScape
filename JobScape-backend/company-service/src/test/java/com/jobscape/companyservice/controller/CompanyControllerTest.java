package com.jobscape.companyservice.controller;

import com.jobscape.companyservice.dto.CompanyDto;
import com.jobscape.companyservice.service.CompanyService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CompanyControllerTest {

    private final CompanyService service = mock(CompanyService.class);
    private final CompanyController controller = new CompanyController(service);

    @Test
    void getsCompaniesThroughService() {
        List<CompanyDto> companies = List.of(new CompanyDto());
        when(service.getCompanies()).thenReturn(companies);

        assertEquals(companies, controller.getCompanies());
        verify(service).getCompanies();
    }

    @Test
    void getsCompanyByIdThroughService() {
        CompanyDto company = new CompanyDto();
        when(service.getCompanyById(7L)).thenReturn(company);

        assertEquals(company, controller.getCompanyById(7L));
        verify(service).getCompanyById(7L);
    }

    @Test
    void createsCompanyThroughService() {
        CompanyDto company = new CompanyDto();
        company.setName("New startup");
        company.setLogoUrl("https://example.com/logo.png");
        when(service.createCompany(company)).thenReturn(company);

        assertEquals(company, controller.createCompany(company));
        verify(service).createCompany(company);
    }

    @Test
    void deletesCompanyThroughService() {
        controller.deleteCompany(7L);

        verify(service).deleteCompany(7L);
    }
}
