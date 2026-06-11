package com.jobscape.user.controller;

import com.jobscape.user.client.UserClient;
import com.jobscape.user.exception.UserEmailAlreadyExistException;
import com.jobscape.user.model.User;
import com.jobscape.user.service.UserService;
import com.jobscape.user.service.dto.RegisterRequest;
import com.jobscape.user.service.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/")
    public List<UserResponse> getUserByEmail(@RequestParam(required = false) String email) {
        throw new UserEmailAlreadyExistException();
//        userService.getUserByEmail(email);
    }

    @PostMapping("/add")
    public UserResponse addUser(@RequestBody RegisterRequest register) {
       return userService.createUser(register);
    }

//    @PostMapping("/delete")
//    public void deleteUser(@RequestBody User user) {
//        userClient.delete(user);
//    }


}
