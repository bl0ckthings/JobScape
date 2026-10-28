package com.jobscape.entitymanager.service.mapper;

import com.jobscape.entitymanager.dto.CompanyDto;
import com.jobscape.entitymanager.dto.ContactSummaryDto;
import com.jobscape.entitymanager.model.Company;
import com.jobscape.entitymanager.model.Contact;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public CompanyDto toDto(Company company) {
        CompanyDto dto = new CompanyDto();

        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setWebsite(company.getWebsite());
        dto.setCreatedAt(company.getCreatedAt());
        dto.setUpdatedAt(company.getUpdatedAt());
        dto.setDomain(company.getDomain());
        dto.setLogoUrl(company.getLogoUrl());
        dto.setContacts(company.getContacts().stream()
                .map(this::toSummaryDto)
                .toList());

        return dto;
    }

    public Company toEntity(CompanyDto dto) {
        Company company = new Company();

        company.setName(dto.getName());
        company.setWebsite(dto.getWebsite());
        company.setDomain(dto.getDomain());
        company.setLogoUrl(dto.getLogoUrl());

        return company;
    }

    private ContactSummaryDto toSummaryDto(Contact contact) {
        ContactSummaryDto dto = new ContactSummaryDto();

        dto.setId(contact.getId());
        dto.setName(contact.getName());
        dto.setPhone(contact.getPhone());
        dto.setEmail(contact.getEmail());
        dto.setRole(contact.getRole());

        return dto;
    }
}
