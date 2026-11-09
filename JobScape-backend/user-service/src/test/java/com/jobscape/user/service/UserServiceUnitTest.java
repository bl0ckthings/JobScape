package com.jobscape.user.service;

import com.jobscape.user.client.UserClient;
import com.jobscape.user.exception.UserEmailAlreadyExistException;
import com.jobscape.user.exception.UserNotFoundException;
import com.jobscape.user.model.User;
import com.jobscape.user.service.dto.RegisterRequest;
import com.jobscape.user.service.mapper.UserMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceUnitTest {
    private final UserClient client = mock(UserClient.class);
    private final UserService service = new UserService(client, new UserMapper());

    @Test
    void createsUserWithAvailableEmail() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("Oualid");
        request.setLastName("Test");
        request.setEmail("oualid@example.test");
        request.setPassword("hash-de-test");
        when(client.findByEmail(request.getEmail())).thenReturn(List.of());

        var result = service.createUser(request);

        assertEquals(request.getEmail(), result.getEmail());
        verify(client).save(argThat(user -> "hash-de-test".equals(user.getPassword())));
    }

    @Test
    void rejectsExistingEmail() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("oualid@example.test");
        when(client.findByEmail(request.getEmail())).thenReturn(List.of(new User()));

        assertThrows(UserEmailAlreadyExistException.class, () -> service.createUser(request));
        verify(client, never()).save(any());
    }

    @Test
    void rejectsLoginForMissingUser() {
        when(client.findByEmail("absent@example.test")).thenReturn(List.of());

        assertThrows(UserNotFoundException.class,
                () -> service.checkUserLogin("absent@example.test"));
    }
}
