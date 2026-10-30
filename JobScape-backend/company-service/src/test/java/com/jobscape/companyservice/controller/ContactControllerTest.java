package com.jobscape.companyservice.controller;

import com.jobscape.companyservice.client.ContactClient;
import com.jobscape.companyservice.dto.ContactDto;
import com.jobscape.companyservice.exception.GlobalExceptionHandler;
import com.jobscape.companyservice.service.ContactService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ContactControllerTest {
    private final ContactClient client = mock(ContactClient.class);
    private final MockMvc mvc = MockMvcBuilders.standaloneSetup(new ContactController(new ContactService(client)))
            .setControllerAdvice(new GlobalExceptionHandler()).build();

    @Test void exposesScopedReadRoutes() throws Exception {
        when(client.getContactById(8L)).thenReturn(contact(1L));
        when(client.getContactsByCompany(7L, 1L)).thenReturn(List.of(contact(1L)));
        when(client.getContactsByUserId(1L)).thenReturn(List.of());
        mvc.perform(get("/contacts/8").param("userId", "1"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(8))
                .andExpect(jsonPath("$.userId").doesNotExist());
        mvc.perform(get("/contacts/company/7").param("userId", "1"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("Recruiter"));
        mvc.perform(get("/contacts/user/1"))
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test void exposesCreateUpdateAndDeleteWithTrustedOwner() throws Exception {
        when(client.getContactById(8L)).thenReturn(contact(1L));
        when(client.createContact(any())).thenAnswer(call -> call.getArgument(0));
        when(client.updateContact(eq(8L), any())).thenAnswer(call -> call.getArgument(1));
        String body = "{\"name\":\"Recruiter\",\"userId\":99}";
        mvc.perform(post("/contacts/create").param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.name").value("Recruiter"))
                .andExpect(jsonPath("$.userId").doesNotExist())
                .andExpect(jsonPath("$.company").doesNotExist());
        mvc.perform(put("/contacts/update/8").param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Recruiter"))
                .andExpect(jsonPath("$.userId").doesNotExist())
                .andExpect(jsonPath("$.company").doesNotExist());
        mvc.perform(delete("/contacts/delete/8").param("userId", "1"))
                .andExpect(status().isNoContent());
        verify(client).deleteContactById(8L);
    }

    @Test void returnsNotFoundForAnotherUsersContact() throws Exception {
        when(client.getContactById(8L)).thenReturn(contact(2L));
        mvc.perform(get("/contacts/8").param("userId", "1"))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.code").value("CONTACT_NOT_FOUND"));
    }

    @Test void requiresUserAndRejectsBlankName() throws Exception {
        mvc.perform(get("/contacts/company/7")).andExpect(status().isBadRequest());
        mvc.perform(post("/contacts/create").param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\" \"}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value("INVALID_CONTACT"));
        verifyNoInteractions(client);
    }

    private ContactDto contact(Long owner) {
        ContactDto dto = new ContactDto();
        dto.setId(8L);
        dto.setUserId(owner);
        dto.setName("Recruiter");
        return dto;
    }
}
