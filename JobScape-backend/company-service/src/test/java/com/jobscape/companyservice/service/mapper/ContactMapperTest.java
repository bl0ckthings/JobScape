package com.jobscape.companyservice.service.mapper;

import com.jobscape.companyservice.dto.ContactDto;
import com.jobscape.companyservice.dto.ContactSummaryDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ContactMapperTest {
    private final ContactMapper mapper = new ContactMapper();

    @Test
    void mapsContactToSummary() {
        ContactDto contact = new ContactDto();
        contact.setId(8L);
        contact.setName("Marie");
        contact.setPhone("0102030405");
        contact.setEmail("marie@example.com");
        contact.setRole("RH");

        ContactSummaryDto summary = mapper.toSummaryDto(contact);

        assertEquals(8L, summary.getId());
        assertEquals("Marie", summary.getName());
        assertEquals("0102030405", summary.getPhone());
        assertEquals("marie@example.com", summary.getEmail());
        assertEquals("RH", summary.getRole());
    }

    @Test
    void preservesAbsentOptionalFields() {
        ContactDto contact = new ContactDto();
        contact.setId(9L);
        contact.setName("Alex");

        ContactSummaryDto summary = mapper.toSummaryDto(contact);

        assertEquals(9L, summary.getId());
        assertEquals("Alex", summary.getName());
        assertNull(summary.getPhone());
        assertNull(summary.getEmail());
        assertNull(summary.getRole());
    }
}
