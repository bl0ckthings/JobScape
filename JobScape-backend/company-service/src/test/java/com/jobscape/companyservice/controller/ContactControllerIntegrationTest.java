package com.jobscape.companyservice.controller;

import com.jobscape.companyservice.client.CompanyClient;
import com.jobscape.companyservice.client.ContactClient;
import com.jobscape.companyservice.dto.ContactDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"spring.cloud.consul.enabled=false", "spring.cloud.consul.discovery.enabled=false"})
@AutoConfigureMockMvc
class ContactControllerIntegrationTest {
    @Autowired private MockMvc mvc;
    @MockitoBean private CompanyClient companies;
    @MockitoBean private ContactClient contacts;
    private ContactDto contact;

    @BeforeEach
    void setUpContact() {
        contact = new ContactDto();
        contact.setId(12L);
        contact.setUserId(7L);
        contact.setName("Recruteur");
        contact.setPhone("0102030405");
        contact.setEmail("rh@acme.test");
        contact.setRole("RH");
        when(contacts.getContactById(12L)).thenReturn(contact);
    }

    @Test
    void createsAndRetrievesContactWithRealMapping() throws Exception {
        when(contacts.createContact(any())).thenAnswer(call -> {
            ContactDto created = call.getArgument(0);
            created.setId(12L);
            return created;
        });
        when(contacts.getContactsByCompany(3L, 7L)).thenReturn(List.of(contact));

        mvc.perform(post("/contacts/create").param("userId", "7").contentType("application/json")
                        .content("{\"name\":\" Recruteur \",\"email\":\"rh@acme.test\",\"phone\":\"0102030405\",\"role\":\"RH\",\"userId\":99,\"company\":{\"id\":3}}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Recruteur"))
                .andExpect(jsonPath("$.role").value("RH"))
                .andExpect(jsonPath("$.userId").doesNotExist());
        verify(contacts).createContact(argThat(dto -> Long.valueOf(7).equals(dto.getUserId())));
        mvc.perform(get("/contacts/12").param("userId", "7")).andExpect(jsonPath("$.id").value(12));
        mvc.perform(get("/contacts/company/3").param("userId", "7"))
                .andExpect(jsonPath("$[0].email").value("rh@acme.test"));
    }

    @Test
    void partiallyUpdatesAndDeletesOwnedContact() throws Exception {
        when(contacts.updateContact(eq(12L), any())).thenAnswer(call -> call.getArgument(1));

        mvc.perform(put("/contacts/update/12").param("userId", "7").contentType("application/json")
                        .content("{\"email\":\"nouveau@acme.test\",\"userId\":99}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("nouveau@acme.test"));
        verify(contacts).updateContact(eq(12L), argThat(dto -> dto.getUserId().equals(7L)));
        mvc.perform(delete("/contacts/delete/12").param("userId", "7"))
                .andExpect(status().isNoContent());
        verify(contacts).deleteContactById(12L);
    }

    @Test
    void rejectsInvalidContactOrMissingUserIdentity() throws Exception {
        mvc.perform(post("/contacts/create").param("userId", "7")
                        .contentType("application/json").content("{\"name\":\" \"}"))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/contacts/user/0")).andExpect(status().isBadRequest());
        mvc.perform(get("/contacts/12")).andExpect(status().isBadRequest());
        verify(contacts, never()).createContact(any());
    }

    @Test
    void doesNotUpdateAnotherUsersContact() throws Exception {
        contact.setUserId(99L);
        mvc.perform(put("/contacts/update/12").param("userId", "7")
                        .contentType("application/json").content("{\"name\":\"Intrusion\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("CONTACT_NOT_FOUND"));
        verify(contacts, never()).updateContact(anyLong(), any());
    }
}
