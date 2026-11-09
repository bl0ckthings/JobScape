package com.jobscape.companyservice.controller;

import com.jobscape.companyservice.client.CompanyClient;
import com.jobscape.companyservice.client.ContactClient;
import com.jobscape.companyservice.dto.CompanyDto;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"spring.cloud.consul.enabled=false", "spring.cloud.consul.discovery.enabled=false"})
@AutoConfigureMockMvc
class CompanyControllerIntegrationTest {
    @Autowired private MockMvc mvc;
    @MockitoBean private CompanyClient companies;
    @MockitoBean private ContactClient contacts;

    @Test
    void createsCompanyWithNormalizedDomain() throws Exception {
        CompanyDto company = new CompanyDto();
        company.setId(3L);
        company.setName("Acme");
        company.setDomain("acme.com");
        when(companies.createCompany(any())).thenReturn(company);
        when(companies.getCompanyByDomain("acme.com")).thenReturn(company);

        mvc.perform(post("/companies/create").contentType("application/json")
                        .content("{\"name\":\"Acme\",\"domain\":\"https://www.acme.com/jobs\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(3));
        verify(companies).createCompany(argThat(dto -> "acme.com".equals(dto.getDomain())));
        mvc.perform(get("/companies/domain").param("domain", "acme.com"))
                .andExpect(status().isOk());
    }

    @Test
    void createsCompanyWhenDomainDoesNotExist() throws Exception {
        when(companies.getCompanyByDomain("acme.com")).thenThrow(mock(FeignException.NotFound.class));
        when(companies.createCompany(any())).thenAnswer(call -> call.getArgument(0));

        mvc.perform(post("/companies/resolve").contentType("application/json")
                        .content("{\"name\":\"Acme\",\"domain\":\"acme.com\",\"logoUrl\":\"https://acme.com/logo.png\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.domain").value("acme.com"))
                .andExpect(jsonPath("$.logoUrl").value("https://acme.com/logo.png"));
    }

    @Test
    void rejectsBlankNameAndInvalidDomainProtocol() throws Exception {
        mvc.perform(post("/companies/resolve").contentType("application/json")
                        .content("{\"name\":\" \"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_COMPANY"));
        mvc.perform(get("/companies/domain").param("domain", "ftp://acme.com"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(companies);
    }

    @Test
    void returnsNotFoundWhenDomainSearchHasNoMatch() throws Exception {
        when(companies.getCompanyByDomain("absent.test")).thenThrow(mock(FeignException.NotFound.class));

        mvc.perform(get("/companies/domain").param("domain", "absent.test"))
                .andExpect(status().isNotFound());
    }
}
