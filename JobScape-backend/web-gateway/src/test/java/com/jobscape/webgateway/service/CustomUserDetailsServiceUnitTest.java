package com.jobscape.webgateway.service;

import com.jobscape.webgateway.client.UserClient;
import com.jobscape.webgateway.service.authDto.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceUnitTest {
    private final UserClient users = mock(UserClient.class);
    private final CustomUserDetailsService service = new CustomUserDetailsService(users);

    @Test
    void providesPasswordHashToSpringSecurity() {
        when(users.checkUser("alice@example.test")).thenReturn(new LoginResponse("alice@example.test", "hash"));
        var user = service.loadUserByUsername("alice@example.test");
        assertEquals("alice@example.test", user.getUsername());
        assertEquals("hash", user.getPassword());
        assertTrue(user.getAuthorities().isEmpty());
    }

    @Test
    void rejectsMissingAccount() {
        when(users.checkUser("absent@example.test")).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("absent@example.test"));
    }
}

