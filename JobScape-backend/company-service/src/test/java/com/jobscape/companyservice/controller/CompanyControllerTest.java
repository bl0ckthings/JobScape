package com.jobscape.companyservice.controller;

import com.jobscape.companyservice.dto.CompanyDto;
import com.jobscape.companyservice.dto.CompanySelectionDto;
import com.jobscape.companyservice.exception.GlobalExceptionHandler;
import com.jobscape.companyservice.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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

    @Test
    void resolvesCompanyThroughService() {
        CompanySelectionDto selection = new CompanySelectionDto();
        selection.setName("Example");
        selection.setDomain("example.com");
        CompanyDto company = new CompanyDto();
        company.setId(7L);
        when(service.resolveCompany(selection)).thenReturn(company);

        assertEquals(company, controller.resolveCompany(selection));
        verify(service).resolveCompany(selection);
    }

    @Test
    void rejectsInvalidCompanySelectionAsBadRequest() throws Exception {
        when(service.resolveCompany(any(CompanySelectionDto.class)))
                .thenThrow(new IllegalArgumentException("Company name is required"));
        MockMvc mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        mvc.perform(post("/companies/resolve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"  \"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("INVALID_COMPANY"))
                .andExpect(jsonPath("$.message").value("Company name is required"));
    }
}
