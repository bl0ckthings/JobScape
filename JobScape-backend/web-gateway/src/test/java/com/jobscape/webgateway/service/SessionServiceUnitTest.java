package com.jobscape.webgateway.service;

import com.jobscape.webgateway.client.UserClient;
import com.jobscape.webgateway.service.authDto.UserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionServiceUnitTest {
    private final UserClient users = mock(UserClient.class);
    private final SessionService service = new SessionService(users);

    @BeforeEach
    void authenticateAlice() {
        var principal = new User("alice@example.test", "hash", List.of());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, List.of()));
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void retrievesProfileFromAuthenticatedIdentity() {
        var alice = new UserResponse(7L, "Alice", "Martin", "alice@example.test");
        when(users.getUser("alice@example.test")).thenReturn(alice);
        assertSame(alice, service.getCurrentUser());
    }

    @Test
    void propagatesProfileRetrievalFailure() {
        var unavailable = new IllegalStateException("User-service indisponible");
        when(users.getUser("alice@example.test")).thenThrow(unavailable);
        assertSame(unavailable, assertThrows(IllegalStateException.class, service::getCurrentUser));
    }
}

