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

class UserServiceUnitTest {
    private final UserRepository users = mock(UserRepository.class);
    private final UserService service = new UserService(users, new UserMapper());

    @Test
    void updatesFirstNameWithoutChangingPassword() {
        User user = new User();
        user.setFirstName("Alice");
        user.setLastName("Test");
        user.setEmail("alice@example.test");
        user.setPassword("hash");
        when(users.findById(7L)).thenReturn(Optional.of(user));
        when(users.save(user)).thenReturn(user);
        UserDto update = new UserDto();
        update.setFirstName("Alicia");

        assertEquals("Alicia", service.update(7L, update).getFirstName());
        assertEquals("hash", user.getPassword());
        assertEquals("alice@example.test", user.getEmail());
    }

    @Test
    void rejectsMissingUser() {
        when(users.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }
}
