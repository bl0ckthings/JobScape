package com.jobscape.companyservice.service.mapper;

import com.jobscape.companyservice.dto.CompanyDto;
import com.jobscape.companyservice.dto.CompanySelectionDto;
import com.jobscape.companyservice.dto.CompanySuggestionDto;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public CompanyDto toDto(CompanySelectionDto selection) {
        CompanyDto company = new CompanyDto();
        company.setName(selection.getName());
        company.setDomain(selection.getDomain());
        company.setLogoUrl(selection.getLogoUrl());
        return company;
    }

    public CompanyDto toDto(CompanySuggestionDto suggestion) {
        CompanyDto company = new CompanyDto();
        company.setName(suggestion.getName());
        company.setDomain(suggestion.getDomain());
        company.setLogoUrl(suggestion.getLogoUrl());
        return company;
    }
}
