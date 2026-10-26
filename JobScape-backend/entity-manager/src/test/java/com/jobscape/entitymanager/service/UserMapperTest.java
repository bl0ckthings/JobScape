package com.jobscape.entitymanager.service;

import com.jobscape.entitymanager.dto.UserDto;
import com.jobscape.entitymanager.model.User;
import com.jobscape.entitymanager.service.mapper.UserMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserMapperTest {

    private final UserMapper mapper = new UserMapper();

    @Test
    void mapsUserToDtoWithoutPassword() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 10, 23, 9, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 10, 23, 10, 0);
        User user = new User();
        user.setId(1L);
        user.setFirstName("Alex");
        user.setLastName("Martin");
        user.setEmail("alex@example.com");
        user.setPassword("encoded-password");
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(updatedAt);

        UserDto dto = mapper.toDto(user);

        assertEquals(1L, dto.getId());
        assertEquals("Alex", dto.getFirstName());
        assertEquals("Martin", dto.getLastName());
        assertEquals("alex@example.com", dto.getEmail());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(updatedAt, dto.getUpdatedAt());
    }

    @Test
    void mapsUserDtoToEntityWithoutServerManagedFields() {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setFirstName("Alex");
        dto.setLastName("Martin");
        dto.setEmail("alex@example.com");
        dto.setCreatedAt(LocalDateTime.of(2026, 10, 23, 9, 0));
        dto.setUpdatedAt(LocalDateTime.of(2026, 10, 23, 10, 0));

        User user = mapper.toEntity(dto);

        assertEquals("Alex", user.getFirstName());
        assertEquals("Martin", user.getLastName());
        assertEquals("alex@example.com", user.getEmail());
        assertNull(user.getId());
        assertNull(user.getPassword());
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
    }
}
