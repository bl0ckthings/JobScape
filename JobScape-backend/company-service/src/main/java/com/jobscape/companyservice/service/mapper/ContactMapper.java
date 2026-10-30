package com.jobscape.companyservice.service.mapper;

import com.jobscape.companyservice.dto.ContactDto;
import com.jobscape.companyservice.dto.ContactSummaryDto;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {

    public static ContactSummaryDto toSummaryDto(ContactDto contact) {
        ContactSummaryDto summary = new ContactSummaryDto();
        summary.setId(contact.getId());
        summary.setName(contact.getName());
        summary.setPhone(contact.getPhone());
        summary.setEmail(contact.getEmail());
        summary.setRole(contact.getRole());
        return summary;
    }
}
