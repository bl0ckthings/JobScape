package com.jobscape.companyservice.service;

import com.jobscape.companyservice.client.ContactClient;
import com.jobscape.companyservice.dto.ContactDto;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContactServiceUnitTest {
    private final ContactClient client = mock(ContactClient.class);
    private final ContactService service = new ContactService(client);

    @Test
    void createsContactWithTrustedOwnerAndIgnoresClientProvidedDates() {
        ContactDto request = new ContactDto();
        request.setId(999L);
        request.setName(" Recruteur ");
        request.setUserId(99L);
        request.setCreatedAt(LocalDateTime.now());
        when(client.createContact(any())).thenAnswer(call -> call.getArgument(0));

        assertEquals("Recruteur", service.createContact(request, 7L).getName());
        verify(client).createContact(argThat(contact -> contact.getUserId().equals(7L)
                && contact.getId() == null && contact.getCreatedAt() == null));
    }

    @Test
    void rejectsReadingAndWritingAnotherUsersContact() {
        ContactDto contact = new ContactDto();
        contact.setUserId(99L);
        when(client.getContactById(12L)).thenReturn(contact);

        assertThrows(ResponseStatusException.class, () -> service.getContactById(12L, 7L));
        assertThrows(ResponseStatusException.class, () -> service.updateContact(12L, 7L, new ContactDto()));
        assertThrows(ResponseStatusException.class, () -> service.deleteContact(12L, 7L));
        verify(client, never()).updateContact(anyLong(), any());
        verify(client, never()).deleteContactById(anyLong());
    }

    @Test
    void translatesMissingContactToNotFound() {
        when(client.getContactById(12L)).thenThrow(mock(FeignException.NotFound.class));

        var error = assertThrows(ResponseStatusException.class, () -> service.getContactById(12L, 7L));

        assertEquals(404, error.getStatusCode().value());
    }
}
