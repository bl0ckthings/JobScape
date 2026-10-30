package com.jobscape.companyservice.service;

import com.jobscape.companyservice.client.ContactClient;
import com.jobscape.companyservice.dto.ContactDto;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContactServiceTest {
    private final ContactClient client = mock(ContactClient.class);
    private final ContactService service = new ContactService(client);

    @Test void listsContactsWithOwnerScope() {
        ContactDto contact = contact(1L);
        when(client.getContactsByCompany(7L, 1L)).thenReturn(List.of(contact));
        when(client.getContactsByUserId(1L)).thenReturn(List.of(contact));
        assertEquals(contact.getId(), service.getContactsByCompany(7L, 1L).get(0).getId());
        assertEquals(contact.getName(), service.getContactsByUserId(1L).get(0).getName());
    }

    @Test void readsOwnedContact() {
        ContactDto contact = contact(1L);
        when(client.getContactById(8L)).thenReturn(contact);
        assertEquals(contact.getId(), service.getContactById(8L, 1L).getId());
    }

    @Test void refusesReadingUpdatingOrDeletingAnotherUsersContact() {
        when(client.getContactById(8L)).thenReturn(contact(2L));
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.getContactById(8L, 1L)).getStatusCode().value());
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.updateContact(8L, 1L, contact(1L))).getStatusCode().value());
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.deleteContact(8L, 1L)).getStatusCode().value());
        verify(client, never()).updateContact(anyLong(), any());
        verify(client, never()).deleteContactById(anyLong());
    }

    @Test void createsWithTrustedOwnerAndWithoutClientSuppliedIdentityOrDates() {
        ContactDto input = contact(99L);
        input.setId(456L);
        input.setCreatedAt(java.time.LocalDateTime.now());
        input.setUpdatedAt(java.time.LocalDateTime.now());
        when(client.createContact(any())).thenAnswer(call -> call.getArgument(0));
        assertEquals("Recruiter", service.createContact(input, 1L).getName());
        verify(client).createContact(argThat(dto -> dto.getUserId().equals(1L)
                && dto.getId() == null && dto.getCreatedAt() == null && dto.getUpdatedAt() == null
                && dto.getName().equals("Recruiter")));
    }

    @Test void updatesAndDeletesOwnedContact() {
        when(client.getContactById(8L)).thenReturn(contact(1L));
        ContactDto changes = contact(99L);
        when(client.updateContact(eq(8L), any())).thenAnswer(call -> call.getArgument(1));
        service.updateContact(8L, 1L, changes);
        verify(client).updateContact(eq(8L), argThat(dto -> dto.getUserId().equals(1L)));
        service.deleteContact(8L, 1L);
        verify(client).deleteContactById(8L);
    }

    @Test void rejectsMissingOwnerBeforeCallingEntityManager() {
        assertThrows(ResponseStatusException.class, () -> service.getContactsByUserId(null));
        assertThrows(ResponseStatusException.class, () -> service.getContactsByCompany(7L, null));
        assertThrows(ResponseStatusException.class, () -> service.getContactById(8L, null));
        assertThrows(ResponseStatusException.class, () -> service.createContact(contact(1L), null));
        assertThrows(ResponseStatusException.class, () -> service.updateContact(8L, null, contact(1L)));
        assertThrows(ResponseStatusException.class, () -> service.deleteContact(8L, null));
        verifyNoInteractions(client);
    }

    @Test void rejectsInvalidCreationAndBlankUpdatedName() {
        ContactDto input = contact(1L);
        input.setName("  ");
        assertThrows(ResponseStatusException.class, () -> service.createContact(input, 1L));
        when(client.getContactById(8L)).thenReturn(contact(1L));
        assertThrows(ResponseStatusException.class, () -> service.updateContact(8L, 1L, input));
        verify(client, never()).createContact(any());
        verify(client, never()).updateContact(anyLong(), any());
    }

    @Test void translatesUnknownContactToNotFound() {
        when(client.getContactById(8L)).thenThrow(mock(feign.FeignException.NotFound.class));
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.getContactById(8L, 1L)).getStatusCode().value());
    }

    @Test void supportsPartialUpdateWithoutChangingTheOriginalRequest() {
        when(client.getContactById(8L)).thenReturn(contact(1L));
        ContactDto changes = new ContactDto();
        changes.setEmail("recruiter@example.com");
        changes.setUserId(99L);
        when(client.updateContact(eq(8L), any())).thenAnswer(call -> call.getArgument(1));
        service.updateContact(8L, 1L, changes);
        verify(client).updateContact(eq(8L), argThat(dto -> dto.getName() == null
                && dto.getEmail().equals("recruiter@example.com") && dto.getUserId().equals(1L)));
        assertEquals(99L, changes.getUserId());
    }

    private ContactDto contact(Long owner) {
        ContactDto contact = new ContactDto();
        contact.setId(8L);
        contact.setUserId(owner);
        contact.setName("Recruiter");
        return contact;
    }
}
