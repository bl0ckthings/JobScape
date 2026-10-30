package com.jobscape.companyservice.service;

import com.jobscape.companyservice.client.ContactClient;
import com.jobscape.companyservice.dto.ContactDto;
import com.jobscape.companyservice.dto.ContactSummaryDto;
import com.jobscape.companyservice.service.mapper.ContactMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactClient contactClient;

    public List<ContactSummaryDto> getContactsByCompany(Long companyId, Long userId) {
        requireId(companyId, "Company id");
        requireId(userId, "User id");
        return contactClient.getContactsByCompany(companyId, userId)
                .stream()
                .map(ContactMapper::toSummaryDto)
                .toList();
    }

    public List<ContactSummaryDto> getContactsByUserId(Long userId) {
        requireId(userId, "User id");
        return contactClient.getContactsByUserId(userId)
                .stream()
                .map(ContactMapper::toSummaryDto)
                .toList();
    }

    public ContactSummaryDto getContactById(Long contactId, Long userId) {
        return ContactMapper.toSummaryDto(getOwnedContact(contactId, userId));
    }

    private ContactDto getOwnedContact(Long contactId, Long userId) {
        requireId(contactId, "Contact id");
        requireId(userId, "User id");
        ContactDto contact;
        try {
            contact = contactClient.getContactById(contactId);
        } catch (FeignException.NotFound exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found");
        }
        if (!userId.equals(contact.getUserId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found");
        }
        return contact;
    }

    public ContactSummaryDto createContact(ContactDto contactDto, Long userId) {
        requireId(userId, "User id");
        validateContact(contactDto, true);
        return ContactMapper.toSummaryDto(contactClient.createContact(writableContact(contactDto, userId)));
    }

    public ContactSummaryDto updateContact(Long contactId, Long userId, ContactDto changes) {
        ContactDto existing = getOwnedContact(contactId, userId);
        validateContact(changes, false);
        return ContactMapper.toSummaryDto(
                contactClient.updateContact(contactId, writableContact(changes, existing.getUserId())));
    }

    public void deleteContact(Long contactId, Long userId) {
        getOwnedContact(contactId, userId);
        contactClient.deleteContactById(contactId);
    }

    private void requireId(Long id, String field) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, field + " must be positive");
        }
    }

    private void validateContact(ContactDto contact, boolean creating) {
        if (contact == null || (creating && contact.getName() == null)
                || (contact.getName() != null && contact.getName().isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contact name is required");
        }
        if (contact.getCompany() != null) {
            requireId(contact.getCompany().getId(), "Company id");
        }
    }

    private ContactDto writableContact(ContactDto source, Long userId) {
        // Les identifiants et dates du formulaire ne doivent pas remplacer les valeurs persistées.
        ContactDto target = new ContactDto();
        target.setName(source.getName() == null ? null : source.getName().trim());
        target.setPhone(source.getPhone());
        target.setEmail(source.getEmail());
        target.setRole(source.getRole());
        target.setCompany(source.getCompany());
        target.setUserId(userId);
        return target;
    }
}
