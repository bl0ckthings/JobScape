package com.jobscape.user.controller;

import com.jobscape.user.client.UserClient;
import com.jobscape.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserClient userClient;

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userClient.findById(id);
    }

    @GetMapping("/")
    public List<User> getUserByEmail(@RequestParam(required = false) String email) {
        return userClient.findByEmail(email);
    }

    @PostMapping("/add")
    public void addUser(@RequestBody User user) {
        userClient.save(user);
    }

    @PostMapping("/delete")
    public void deleteUser(@RequestBody User user) {
        userClient.delete(user);
    }


}
