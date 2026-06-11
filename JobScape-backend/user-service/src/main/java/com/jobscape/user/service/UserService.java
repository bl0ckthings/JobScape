package com.jobscape.user.service;

import com.jobscape.user.client.UserClient;
import com.jobscape.user.exception.UserEmailAlreadyExistException;
import com.jobscape.user.model.User;
import com.jobscape.user.service.dto.RegisterRequest;
import com.jobscape.user.service.dto.UserResponse;
import com.jobscape.user.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserClient userClient;
    private final UserMapper userMapper;


    public UserResponse createUser(RegisterRequest register) {
        if (!userClient.findByEmail(register.getEmail()).isEmpty()) {
            throw new UserEmailAlreadyExistException();
        }
        User user = userMapper.toEntityRegister(register);
        userClient.save(user);
        return userMapper.toDto(user);

    }

    public UserResponse getUserById(Long id) {

        User user = userClient.findById(id);
        return userMapper.toDto(user);
    }

//    public List<UserResponse> getUserByEmail(String email) {
//
//            User user = userClient.findByEmail(email).get(0);
//            UserResponse userResponse = userMapper.toDto(user);
//            return user
//    }


}
