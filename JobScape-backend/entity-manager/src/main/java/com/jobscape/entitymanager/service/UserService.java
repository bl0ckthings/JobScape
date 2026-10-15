package com.jobscape.entitymanager.service;

import com.jobscape.entitymanager.exception.ResourceNotFoundException;
import com.jobscape.entitymanager.model.User;
import com.jobscape.entitymanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return findUser(id);
    }

    @Transactional(readOnly = true)
    public List<User> getAll(String email) {
        if (email == null) {
            return userRepository.findAll();
        }
        return userRepository.findByEmail(email).map(List::of).orElseGet(List::of);
    }

    @Transactional
    public User create(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User update(Long id, User update) {
        User user = findUser(id);
        if (update.getFirstName() != null) {
            user.setFirstName(update.getFirstName());
        }
        if (update.getLastName() != null) {
            user.setLastName(update.getLastName());
        }
        if (update.getEmail() != null) {
            user.setEmail(update.getEmail());
        }
        if (update.getPassword() != null) {
            user.setPassword(update.getPassword());
        }
        return userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.delete(findUser(id));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }
}
