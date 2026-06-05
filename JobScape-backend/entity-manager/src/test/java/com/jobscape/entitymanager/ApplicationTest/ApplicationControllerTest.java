package com.jobscape.entitymanager.ApplicationTest;

import com.jobscape.entitymanager.controller.ApplicationController;
import com.jobscape.entitymanager.model.Application;
import com.jobscape.entitymanager.model.Company;
import com.jobscape.entitymanager.model.User;
import com.jobscape.entitymanager.repository.ApplicationRepository;
import com.jobscape.entitymanager.repository.CompanyRepository;
import com.jobscape.entitymanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import static com.jobscape.entitymanager.model.ApplicationStatus.APPLIED;

@SpringBootTest
@ActiveProfiles("test")
public class ApplicationControllerTest {

    @Autowired
    ApplicationController controller;
    @Autowired
    ApplicationRepository applicationRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    public void CreateNewApplicationTest() {
        // Arrange
        Company company = new Company(
                "Google",
                "google.com"
        );

        User user = new User("Alice", "Martin", "alice@mail.com", "1234");


        Application application = new Application("Backend Dev", "Paris", "France", "LinkedIn", "null", APPLIED, "remote", user, company);
        // Act
        companyRepository.save(company);
        userRepository.save(user);
        Application saved = controller.create(application);


        // Assert
        assertEquals(saved.getUser().getId(), application.getUser().getId());
    }

    @Test
    public void getApplicationByUserIdTest() {
        //Arrange
        Company company = new Company(
                "Google",
                "google.com"
        );

        User user = new User("Alice", "Martin", "alice@mail.com", "1234");

        List<Application> applications = List.of(
                new Application("Backend Dev", "Paris", "France", "LinkedIn", "null", APPLIED, "remote", user, company),
                new Application("Frontend Dev", "Paris", "France", "LinkedIn", "null", APPLIED, "remote", user, company)

                );
        // Act
        companyRepository.save(company);
        userRepository.save(user);
        applicationRepository.saveAll(applications);


        // Assert
        assertEquals(controller.getApplicationsByUserId(user.getId()).size(), applications.size());

    }

}
