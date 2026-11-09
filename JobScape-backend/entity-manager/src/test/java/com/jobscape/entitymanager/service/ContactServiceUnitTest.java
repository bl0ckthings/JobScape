package com.jobscape.entitymanager.service;

import com.jobscape.entitymanager.dto.*;
import com.jobscape.entitymanager.model.*;
import com.jobscape.entitymanager.repository.*;
import com.jobscape.entitymanager.service.mapper.*;
import com.jobscape.entitymanager.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContactServiceUnitTest {
    private final ContactRepository contacts = mock(ContactRepository.class);
    private final ContactService service = new ContactService(contacts,
            mock(CompanyRepository.class), mock(UserRepository.class), new ContactMapper());

    @Test
    void updatesEmailWithoutClearingExistingContactDetails() {
        Contact contact = new Contact();
        contact.setName("Recruteur");
        contact.setPhone("0102030405");
        when(contacts.findById(12L)).thenReturn(Optional.of(contact));
        when(contacts.save(contact)).thenReturn(contact);
        ContactDto update = new ContactDto();
        update.setEmail("rh@example.test");

        var result = service.update(12L, update);

        assertEquals("Recruteur", result.getName());
        assertEquals("0102030405", result.getPhone());
        assertEquals("rh@example.test", result.getEmail());
    }

    @Test
    void rejectsMissingContact() {
        when(contacts.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }
}
