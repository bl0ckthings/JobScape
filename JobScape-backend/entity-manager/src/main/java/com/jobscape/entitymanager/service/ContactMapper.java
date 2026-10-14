package com.jobscape.entitymanager.service;

import com.jobscape.entitymanager.dto.CompanySummaryDto;
import com.jobscape.entitymanager.dto.ContactDto;
import com.jobscape.entitymanager.model.Company;
import com.jobscape.entitymanager.model.Contact;
import com.jobscape.entitymanager.model.User;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {

    public ContactDto toDto(Contact contact) {
        ContactDto dto = new ContactDto();

        dto.setId(contact.getId());
        dto.setName(contact.getName());
        dto.setPhone(contact.getPhone());
        dto.setEmail(contact.getEmail());
        dto.setRole(contact.getRole());
        dto.setCreatedAt(contact.getCreatedAt());
        dto.setUpdatedAt(contact.getUpdatedAt());
        dto.setCompany(contact.getCompany() == null ? null : toSummaryDto(contact.getCompany()));
        dto.setUserId(contact.getUser() == null ? null : contact.getUser().getId());

        return dto;
    }

    public Contact toEntity(ContactDto dto, Company company, User user) {
        Contact contact = new Contact();

        contact.setName(dto.getName());
        contact.setPhone(dto.getPhone());
        contact.setEmail(dto.getEmail());
        contact.setRole(dto.getRole());
        contact.setCompany(company);
        contact.setUser(user);

        return contact;
    }

    private CompanySummaryDto toSummaryDto(Company company) {
        CompanySummaryDto dto = new CompanySummaryDto();

        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setWebsite(company.getWebsite());

        return dto;
    }
}
