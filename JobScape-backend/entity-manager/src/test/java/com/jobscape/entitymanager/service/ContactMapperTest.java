package com.jobscape.entitymanager.service;

import com.jobscape.entitymanager.dto.ContactDto;
import com.jobscape.entitymanager.model.Company;
import com.jobscape.entitymanager.model.Contact;
import com.jobscape.entitymanager.model.User;
import com.jobscape.entitymanager.service.mapper.ContactMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class ContactMapperTest {

    private final ContactMapper mapper = new ContactMapper();

    @Test
    void mapsContactAndCompanySummaryToDto() {
        Company company = new Company();
        company.setId(1L);
        company.setName("JobScape");
        company.setWebsite("https://jobscape.example");

        User user = new User();
        user.setId(2L);

        Contact contact = new Contact();
        contact.setId(3L);
        contact.setName("Alex Martin");
        contact.setPhone("0102030405");
        contact.setEmail("alex@example.com");
        contact.setRole("Recruiter");
        contact.setCompany(company);
        contact.setUser(user);

        ContactDto dto = mapper.toDto(contact);

        assertEquals(3L, dto.getId());
        assertEquals("Alex Martin", dto.getName());
        assertEquals("0102030405", dto.getPhone());
        assertEquals("alex@example.com", dto.getEmail());
        assertEquals("Recruiter", dto.getRole());
        assertEquals(1L, dto.getCompany().getId());
        assertEquals("JobScape", dto.getCompany().getName());
        assertEquals(2L, dto.getUserId());
    }

    @Test
    void mapsContactWithoutOptionalRelationships() {
        ContactDto dto = mapper.toDto(new Contact());

        assertNull(dto.getCompany());
        assertNull(dto.getUserId());
    }

    @Test
    void mapsContactDtoToEntityUsingResolvedRelationships() {
        ContactDto dto = new ContactDto();
        dto.setName("Alex Martin");
        dto.setPhone("0102030405");
        dto.setEmail("alex@example.com");
        dto.setRole("Recruiter");

        Company company = new Company();
        User user = new User();

        Contact contact = mapper.toEntity(dto, company, user);

        assertEquals("Alex Martin", contact.getName());
        assertEquals("0102030405", contact.getPhone());
        assertEquals("alex@example.com", contact.getEmail());
        assertEquals("Recruiter", contact.getRole());
        assertSame(company, contact.getCompany());
        assertSame(user, contact.getUser());
    }
}
