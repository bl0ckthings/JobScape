package com.jobscape.user.service;

import com.jobscape.user.client.UserClient;
import com.jobscape.user.exception.UserEmailAlreadyExistException;
import com.jobscape.user.exception.UserNotFoundException;
import com.jobscape.user.model.User;
import com.jobscape.user.service.dto.LoginResponse;
import com.jobscape.user.service.dto.RegisterRequest;
import com.jobscape.user.service.dto.UserResponse;
import com.jobscape.user.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserClient userClient;
    private final UserMapper userMapper;


    public UserResponse createUser(RegisterRequest register) {
        if (!userClient.findByEmail(register.getEmail()).isEmpty()) {
            throw new UserEmailAlreadyExistException(register.getEmail());
        }
        User user = userMapper.toEntityRegister(register);
        userClient.save(user);
        return userMapper.toDto(user);

    }

    public void deleteUser(Long id) {
        userClient.delete(id);
    }

    public UserResponse getUserById(Long id) {
        User user = userClient.findById(id).orElseThrow(UserNotFoundException::new);
        return userMapper.toDto(user);
    }

    public UserResponse getUserByEmail(String email) {
        if (userClient.findByEmail(email).isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userClient.findByEmail(email).get(0);
        return userMapper.toDto(user);
    }

    public LoginResponse checkUserLogin(String email) {
        if (userClient.findByEmail(email).isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userClient.findByEmail(email).get(0);
        return userMapper.toLoginDto(user);
    }


}
