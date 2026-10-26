package com.jobscape.entitymanager.service.mapper;

import com.jobscape.entitymanager.dto.UserDto;
import com.jobscape.entitymanager.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setUpdatedAt(user.getUpdatedAt());
        userDto.setCreatedAt(user.getCreatedAt());

        return userDto;
    }

    public User toEntity(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());

        return user;
    }
}
