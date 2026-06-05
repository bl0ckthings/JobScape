package com.jobscape.entitymanager.UserTest;

import com.jobscape.entitymanager.controller.UserController;
import com.jobscape.entitymanager.model.User;
import com.jobscape.entitymanager.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    UserController userController;

    @Autowired
    UserRepository userRepository;




    @Test
    public void getUserByEmailTest() {
        // Arrange
        User user = new User(
                null,
                "Amine",
                "Momo",
                "amine.momo@gmail.com",
                "azeazifrg",
                null,
                null

        );
        userRepository.save(user);
        // Act
        List<User> result = userController.findAll("amine.momo@gmail.com");

        // Assert
        assertEquals(result.get(0).getFirstName(), user.getFirstName());
    }

    @Test
    public void getUserById() {
        User user = new User(
                null,
                "Amine",
                "Momo",
                "amine.momo@gmail.com",
                "azeazifrg",
                null,
                null

        );
        userRepository.save(user);
        User result = userController.findById(user.getId());
        assertEquals(result.getId(), user.getId());
    }
}
