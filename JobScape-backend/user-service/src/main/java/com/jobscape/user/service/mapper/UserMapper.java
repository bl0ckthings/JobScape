package com.jobscape.user.service.mapper;

import com.jobscape.user.model.User;
import com.jobscape.user.service.dto.RegisterRequest;
import com.jobscape.user.service.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    public User toEntityRegister(RegisterRequest register) {
        return new User(
                register.getFirstName(),
                register.getLastName(),
                register.getEmail(),
                register.getPassword()
        );
    }



    public UserResponse toDto(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
}
