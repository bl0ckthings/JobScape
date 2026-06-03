package com.jobscape.entitymanager.controller;

import com.jobscape.entitymanager.model.User;
import com.jobscape.entitymanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;


    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @GetMapping
    public List<User> findAll(@RequestParam(required = false) String email) {

        if (email != null) {
            Optional<User> user = userRepository.findByEmail(email);
            return user.isPresent() ? List.of(user.get()) : List.of();
        }
        return userRepository.findAll();
    }

    @PostMapping("/add")
    public User saveUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @DeleteMapping("/delete")
    public  void deleteUser(@RequestParam Long id) {
        userRepository.deleteById(id);
    }
}
