package com.jobscape.entitymanager.ApplicationStepTest;

import com.jobscape.entitymanager.controller.ApplicationStepController;
import com.jobscape.entitymanager.dto.ApplicationStepDto;
import com.jobscape.entitymanager.model.Application;
import com.jobscape.entitymanager.model.ApplicationStep;
import com.jobscape.entitymanager.model.Company;
import com.jobscape.entitymanager.model.User;
import com.jobscape.entitymanager.repository.ApplicationRepository;
import com.jobscape.entitymanager.repository.ApplicationStepRepository;
import com.jobscape.entitymanager.repository.CompanyRepository;
import com.jobscape.entitymanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.jobscape.entitymanager.model.ApplicationStatus.APPLIED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ApplicationStepControllerTest {

    @Autowired ApplicationStepController controller;
    @Autowired ApplicationStepRepository stepRepository;
    @Autowired ApplicationRepository applicationRepository;
    @Autowired CompanyRepository companyRepository;
    @Autowired UserRepository userRepository;

    @Test
    void createApplicationStepTest() {
        Application application = createApplication("step-create@test.com");
        ApplicationStepDto step = new ApplicationStepDto();
        step.setApplicationId(application.getId());
        step.setStepStatus(APPLIED);
        step.setStepDate(LocalDateTime.now());

        ApplicationStepDto saved = controller.createStep(step);

        assertNotNull(saved.getId());
        assertEquals(application.getId(), saved.getApplicationId());
    }

    @Test
    void getStepsByApplicationTest() {
        Application application = createApplication("step-list@test.com");
        ApplicationStep step = new ApplicationStep();
        step.setApplication(application);
        step.setStepStatus(APPLIED);
        stepRepository.save(step);

        List<ApplicationStepDto> result = controller.getStepsByApplicationId(application.getId());

        assertEquals(1, result.size());
    }

    private Application createApplication(String email) {
        User user = userRepository.save(new User("Alice", "Martin", email, "hash"));
        Company company = companyRepository.save(new Company("https://jobscape.test", "JobScape"));
        return applicationRepository.save(
                new Application("Backend Dev", "Paris", "France", "LinkedIn", "url", APPLIED, "remote", user, company)
        );
    }
}
