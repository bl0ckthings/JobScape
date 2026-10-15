package com.jobscape.entitymanager.service;

import com.jobscape.entitymanager.dto.CompanyDto;
import com.jobscape.entitymanager.model.Company;
import com.jobscape.entitymanager.model.Contact;
import com.jobscape.entitymanager.service.mapper.CompanyMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CompanyMapperTest {

    private final CompanyMapper mapper = new CompanyMapper();

    @Test
    void mapsCompanyAndContactSummariesToDto() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 10, 14, 9, 0);
        Company company = new Company();
        company.setId(1L);
        company.setName("JobScape");
        company.setWebsite("https://jobscape.example");
        company.setCreatedAt(createdAt);

        Contact contact = new Contact();
        contact.setId(2L);
        contact.setName("Alex Martin");
        contact.setEmail("alex@example.com");
        company.getContacts().add(contact);

        CompanyDto dto = mapper.toDto(company);

        assertEquals(1L, dto.getId());
        assertEquals("JobScape", dto.getName());
        assertEquals("https://jobscape.example", dto.getWebsite());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(1, dto.getContacts().size());
        assertEquals(2L, dto.getContacts().get(0).getId());
        assertEquals("Alex Martin", dto.getContacts().get(0).getName());
    }

    @Test
    void mapsCompanyDtoToEntity() {
        CompanyDto dto = new CompanyDto();
        dto.setName("JobScape");
        dto.setWebsite("https://jobscape.example");

        Company company = mapper.toEntity(dto);

        assertEquals("JobScape", company.getName());
        assertEquals("https://jobscape.example", company.getWebsite());
    }
}
