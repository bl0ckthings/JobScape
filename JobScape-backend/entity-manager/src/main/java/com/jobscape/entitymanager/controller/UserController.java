package com.jobscape.entitymanager.controller;

import com.jobscape.entitymanager.dto.UserDto;
import com.jobscape.entitymanager.model.User;
import com.jobscape.entitymanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("internal/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping
    public List<User> findAll(@RequestParam(required = false) String email) {
        return userService.getAll(email);
    }

    @PostMapping("/add")
    public User saveUser(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping("/update/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto user) {
        return userService.update(id, user);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}
