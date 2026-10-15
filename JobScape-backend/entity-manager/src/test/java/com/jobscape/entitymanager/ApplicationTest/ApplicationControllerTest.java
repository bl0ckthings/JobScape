package com.jobscape.entitymanager.ApplicationTest;

import com.jobscape.entitymanager.controller.ApplicationController;
import com.jobscape.entitymanager.dto.ApplicationDto;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.jobscape.entitymanager.model.ApplicationStatus.APPLIED;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
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
                "google.com",
                "Google"
        );

        User user = new User("Alice", "Martin", "alice@mail.com", "1234");


        companyRepository.save(company);
        userRepository.save(user);
        ApplicationDto application = new ApplicationDto();
        application.setJobTitle("Backend Dev");
        application.setCity("Paris");
        application.setCountry("France");
        application.setSource("LinkedIn");
        application.setUrl("null");
        application.setStatus(APPLIED);
        application.setWorkMode("remote");
        application.setUserId(user.getId());
        application.setCompanyId(company.getId());

        ApplicationDto saved = controller.create(application);


        // Assert
        assertEquals(user.getId(), saved.getUserId());
    }

    @Test
    public void getApplicationByUserIdTest() {
        //Arrange
        Company company = new Company(
                "google.com",
                "Google"
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
        assertEquals(applications.size(), controller.getApplicationsByUserId(user.getId()).size());

    }

}
