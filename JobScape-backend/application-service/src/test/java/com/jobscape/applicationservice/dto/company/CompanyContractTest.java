package com.jobscape.applicationservice.dto.company;

import com.jobscape.applicationservice.client.CompanyClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CompanyContractTest {

    @Test
    void companyResolveUsesSelectionAndReturnsCompany() throws NoSuchMethodException {
        CompanySelectionDto selection = new CompanySelectionDto();
        selection.setName("JobScape");
        selection.setDomain("jobscape.com");
        selection.setLogoUrl("https://example.com/logo.png");

        CompanyDto company = new CompanyDto();
        company.setId(13L);
        company.setName(selection.getName());
        company.setDomain(selection.getDomain());
        company.setLogoUrl(selection.getLogoUrl());

        assertEquals("JobScape", selection.getName());
        assertEquals(13L, company.getId());
        assertEquals(selection.getDomain(), company.getDomain());
        assertEquals(CompanyDto.class,
                CompanyClient.class.getMethod("resolveCompany", CompanySelectionDto.class).getReturnType());
    }
}
