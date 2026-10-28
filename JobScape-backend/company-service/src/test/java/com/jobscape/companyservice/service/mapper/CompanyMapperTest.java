package com.jobscape.companyservice.service.mapper;

import com.jobscape.companyservice.dto.CompanyDto;
import com.jobscape.companyservice.dto.CompanySelectionDto;
import com.jobscape.companyservice.dto.CompanySuggestionDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CompanyMapperTest {

    private final CompanyMapper mapper = new CompanyMapper();

    @Test
    void mapsSelectionToNewCompanyDto() {
        CompanySelectionDto selection = new CompanySelectionDto();
        selection.setName("Example");
        selection.setDomain("example.com");
        selection.setLogoUrl("https://example.com/logo.png");

        CompanyDto company = mapper.toDto(selection);

        assertEquals("Example", company.getName());
        assertEquals("example.com", company.getDomain());
        assertEquals("https://example.com/logo.png", company.getLogoUrl());
        assertNull(company.getId());
        assertNull(company.getWebsite());
        assertNull(company.getCreatedAt());
        assertNull(company.getUpdatedAt());
    }

    @Test
    void mapsSuggestionToNewCompanyDtoWithOptionalDomain() {
        CompanySuggestionDto suggestion = new CompanySuggestionDto();
        suggestion.setName("New startup");
        suggestion.setLogoUrl("https://example.com/startup.png");

        CompanyDto company = mapper.toDto(suggestion);

        assertEquals("New startup", company.getName());
        assertNull(company.getDomain());
        assertEquals("https://example.com/startup.png", company.getLogoUrl());
        assertNull(company.getId());
    }
}
