package com.jobscape.entitymanager.ReminderTest;

import com.jobscape.entitymanager.controller.ReminderController;
import com.jobscape.entitymanager.dto.ReminderDto;
import com.jobscape.entitymanager.model.Application;
import com.jobscape.entitymanager.model.Company;
import com.jobscape.entitymanager.model.Reminder;
import com.jobscape.entitymanager.model.User;
import com.jobscape.entitymanager.repository.ApplicationRepository;
import com.jobscape.entitymanager.repository.CompanyRepository;
import com.jobscape.entitymanager.repository.ReminderRepository;
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
class ReminderControllerTest {

    @Autowired ReminderController controller;
    @Autowired ReminderRepository reminderRepository;
    @Autowired ApplicationRepository applicationRepository;
    @Autowired CompanyRepository companyRepository;
    @Autowired UserRepository userRepository;

    @Test
    void createReminderTest() {
        Application application = createApplication("reminder-create@test.com");
        ReminderDto reminder = new ReminderDto();
        reminder.setApplicationId(application.getId());
        reminder.setReminderDate(LocalDateTime.now().plusDays(1));
        reminder.setComment("Follow up");

        ReminderDto saved = controller.createReminder(reminder);

        assertNotNull(saved.getId());
        assertEquals(application.getId(), saved.getApplicationId());
    }

    @Test
    void getRemindersByApplicationTest() {
        Application application = createApplication("reminder-list@test.com");
        Reminder reminder = new Reminder();
        reminder.setApplication(application);
        reminder.setReminderDate(LocalDateTime.now().plusDays(1));
        reminderRepository.save(reminder);

        List<ReminderDto> result = controller.getReminderByApplication(application.getId());

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
