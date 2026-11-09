package com.jobscape.webgateway.controller;

import com.jobscape.webgateway.client.ApplicationClient;
import com.jobscape.webgateway.client.CompanyClient;
import com.jobscape.webgateway.client.UserClient;
import com.jobscape.webgateway.configuration.JwtUtils;
import com.jobscape.webgateway.service.applicationDto.*;
import com.jobscape.webgateway.service.authDto.LoginResponse;
import com.jobscape.webgateway.service.authDto.RegisterRequest;
import com.jobscape.webgateway.service.authDto.UserResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.cloud.consul.enabled=false",
        "spring.cloud.consul.discovery.enabled=false",
        "jwt.secret=test-only-signing-key-at-least-32-bytes-long",
        "jwt.expiration-time=60000"
})
@AutoConfigureMockMvc
class GatewayControllerIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired JwtUtils jwt;
    @Autowired PasswordEncoder passwords;
    @Autowired ObjectMapper json;
    @MockitoBean UserClient users;
    @MockitoBean ApplicationClient applications;
    @MockitoBean CompanyClient companies;

    @BeforeEach
    void setUpAliceAccount() {
        when(users.checkUser("alice@example.test"))
                .thenReturn(new LoginResponse("alice@example.test", passwords.encode("test-password")));
    }

    @Test
    void authenticatesWithBCryptAndReturnsSignedJwt() throws Exception {
        when(users.getUser("alice@example.test"))
                .thenReturn(new UserResponse(7L, "Alice", "Martin", "alice@example.test"));

        String response = mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"alice@example.test\",\"password\":\"test-password\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.type").value("Bearer"))
                .andReturn().getResponse().getContentAsString();
        String token = json.readTree(response).get("token").asText();
        assertEquals(7L, jwt.extractUserId(token));
        assertEquals("alice@example.test", jwt.extractUsername(token));

    }

    @Test
    void rejectsWrongPasswordWithoutIssuingJwt() throws Exception {
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"alice@example.test\",\"password\":\"incorrect\"}"))
                .andExpect(status().isUnauthorized()).andExpect(content().string("Invalid username or password"));
        verify(users, never()).getUser(anyString());
    }

    @Test
    void hashesPasswordBeforeForwardingRegistration() throws Exception {
        when(users.createUser(any())).thenReturn(new UserResponse(7L, "Alice", "Martin", "alice@example.test"));
        mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"firstName":"Alice","lastName":"Martin","email":"alice@example.test","password":"test-password"}
                        """))
                .andExpect(status().isOk()).andExpect(jsonPath("$.password").doesNotExist());
        var registration = ArgumentCaptor.forClass(RegisterRequest.class);
        verify(users).createUser(registration.capture());
        assertNotEquals("test-password", registration.getValue().getPassword());
        assertTrue(passwords.matches("test-password", registration.getValue().getPassword()));
    }

    @Test
    void rejectsExpiredOrMalformedJwtBeforeCallingBusinessService() throws Exception {
        var key = new SecretKeySpec("test-only-signing-key-at-least-32-bytes-long".getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        String expired = Jwts.builder().setSubject("alice@example.test").claim("userId", 7L)
                .setExpiration(new Date(1)).signWith(key, SignatureAlgorithm.HS256).compact();
        mvc.perform(get("/api/application/user").header("Authorization", "Bearer " + expired))
                .andExpect(status().isUnauthorized()).andExpect(jsonPath("$.error").value("INVALID_OR_EXPIRED_TOKEN"));
        mvc.perform(get("/api/application/user").header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
        verifyNoInteractions(applications);
    }

    @Test
    void allowsPublicHealthCheckAndProtectsOtherRoutes() throws Exception {
        mvc.perform(get("/actuator/health").servletPath("/actuator/health")
                .header("Authorization", "Bearer invalid-token")).andExpect(status().isOk());
        // La configuration actuelle renvoie 403 pour une requête sans authentification.
        mvc.perform(get("/api/application/user")).andExpect(status().isForbidden());
        mvc.perform(get("/actuator/env")).andExpect(status().isForbidden());
        verifyNoInteractions(applications);
    }

    @Test
    void replacesRequestUserIdWithJwtIdentity() throws Exception {
        var application = new ApplicationDto();
        application.setId(42L);
        when(applications.createApplication(any())).thenReturn(application);
        mvc.perform(post("/api/application/create").header("Authorization", bearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":999,\"jobTitle\":\"Développeur\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(42));
        var request = ArgumentCaptor.forClass(CreateApplicationRequest.class);
        verify(applications).createApplication(request.capture());
        assertEquals(7L, request.getValue().getUserId());
    }

    @Test
    void rejectsAnotherUsersApplication() throws Exception {
        var foreign = new ApplicationDto();
        foreign.setId(43L);
        foreign.setUserId(8L);
        when(applications.getApplicationById(43L)).thenReturn(foreign);
        mvc.perform(get("/api/application/43").header("Authorization", bearerToken())).andExpect(status().isNotFound());
    }

    @Test
    void acceptsLogoutRequest() throws Exception {
        mvc.perform(post("/api/auth/logout").servletPath("/api/auth/logout"))
                .andExpect(status().isNoContent());
    }

    private String bearerToken() {
        return "Bearer " + jwt.generateToken(7L, "alice@example.test");
    }
}

