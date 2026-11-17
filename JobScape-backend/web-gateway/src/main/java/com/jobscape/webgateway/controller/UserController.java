package com.jobscape.webgateway.controller;

import com.jobscape.webgateway.client.UserClient;
import com.jobscape.webgateway.service.authDto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    @GetMapping("/me")
    public UserResponse getCurrentUser(@RequestAttribute("authenticatedUserId") Long userId) {
        return userClient.getUserById(userId);
    }
}
