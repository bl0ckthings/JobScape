package com.jobscape.user.controller;

import com.jobscape.user.client.UserClient;
import com.jobscape.user.model.User;
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
class UserControllerIntegrationTest {
    @Autowired private MockMvc mvc;
    @MockitoBean private UserClient client;
    private User user;

    @BeforeEach
    void setUpUser() {
        user = new User();
        user.setFirstName("Oualid");
        user.setLastName("Test");
        user.setEmail("oualid@example.test");
        user.setPassword("hash-de-test");
        user.setId(7L);
    }

    @Test
    void registersAndRetrievesUserWithoutExposingPassword() throws Exception {
        when(client.findByEmail(user.getEmail())).thenReturn(List.of());
        mvc.perform(post("/users/add").contentType("application/json").content("""
                {"firstName":"Oualid","lastName":"Test","email":"oualid@example.test","password":"hash-de-test"}
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.password").doesNotExist());

        when(client.findByEmail(user.getEmail())).thenReturn(List.of(user));
        mvc.perform(get("/users").param("email", user.getEmail())).andExpect(status().isOk())
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void returnsConflictForExistingEmail() throws Exception {
        when(client.findByEmail(user.getEmail())).thenReturn(List.of(user));
        mvc.perform(post("/users/add").contentType("application/json")
                        .content("{\"email\":\"oualid@example.test\"}"))
                .andExpect(status().isConflict());
        verify(client, never()).save(any());
    }

    @Test
    void loadsCredentialsForGatewayAndReportsMissingUser() throws Exception {
        when(client.findByEmail(user.getEmail())).thenReturn(List.of(user));
        mvc.perform(get("/users/checkUser").param("email", user.getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").value("hash-de-test"));

        mvc.perform(get("/users").param("email", "absent@example.test"))
                .andExpect(status().isNotFound());
    }

}
