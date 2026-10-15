package com.jobscape.entitymanager.UserTest;

import com.jobscape.entitymanager.controller.UserController;
import com.jobscape.entitymanager.model.User;
import com.jobscape.entitymanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
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
    public void createUserTest() {
        User user = new User(
                "Amine",
                "Momo",
                "new.amine.momo@gmail.com",
                "azeazifrg"
        );

        User result = userController.saveUser(user);

        assertNotNull(result.getId());
        assertEquals(user.getEmail(), result.getEmail());
    }
}
