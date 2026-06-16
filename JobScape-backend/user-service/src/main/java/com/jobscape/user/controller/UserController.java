package com.jobscape.user.controller;


import com.jobscape.user.service.UserService;
import com.jobscape.user.service.dto.LoginResponse;
import com.jobscape.user.service.dto.RegisterRequest;
import com.jobscape.user.service.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/")
    public UserResponse getUserByEmail(@RequestParam(required = false) String email) {
       return userService.getUserByEmail(email);
    }

    @GetMapping("/checkUser")
    public LoginResponse checkUser(@RequestParam String email) {
        return userService.checkUserLogin(email);
    }

    @PostMapping("/add")
    public UserResponse addUser(@RequestBody RegisterRequest register) {
       return userService.createUser(register);
    }

    @PostMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }


}
