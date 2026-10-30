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
import com.jobscape.entitymanager.exception.GlobalExceptionHandler;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @Test
    void ownerCanUpdateReminderWithoutChangingItsApplication() throws Exception {
        Application application = createApplication("reminder-owner@test.com");
        Reminder reminder = createReminder(application);
        mvc().perform(put("/internal/reminders/update/" + reminder.getId())
                        .param("userId", application.getUser().getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment\":\"Updated\",\"applicationId\":999999}"))
                .andExpect(status().isOk());
        Reminder updated = reminderRepository.findById(reminder.getId()).orElseThrow();
        assertEquals("Updated", updated.getComment());
        assertEquals(application.getId(), updated.getApplication().getId());
    }

    @Test
    void anotherUserCannotUpdateReminder() throws Exception {
        Application application = createApplication("reminder-private-update@test.com");
        User other = userRepository.save(new User("Bob", "Martin", "reminder-other-update@test.com", "hash"));
        Reminder reminder = createReminder(application);
        mvc().perform(put("/internal/reminders/update/" + reminder.getId())
                        .param("userId", other.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON).content("{\"comment\":\"Hacked\"}"))
                .andExpect(status().isNotFound());
        assertEquals("Original", reminderRepository.findById(reminder.getId()).orElseThrow().getComment());
    }

    @Test
    void anotherUserCannotDeleteReminder() throws Exception {
        Application application = createApplication("reminder-private-delete@test.com");
        User other = userRepository.save(new User("Bob", "Martin", "reminder-other-delete@test.com", "hash"));
        Reminder reminder = createReminder(application);
        mvc().perform(delete("/internal/reminders/delete").param("id", reminder.getId().toString())
                        .param("userId", other.getId().toString()))
                .andExpect(status().isNotFound());
        assertTrue(reminderRepository.existsById(reminder.getId()));
    }

    @Test
    void ownerCanDeleteReminder() throws Exception {
        Application application = createApplication("reminder-delete@test.com");
        Reminder reminder = createReminder(application);
        mvc().perform(delete("/internal/reminders/delete").param("id", reminder.getId().toString())
                        .param("userId", application.getUser().getId().toString()))
                .andExpect(status().isOk());
        assertFalse(reminderRepository.existsById(reminder.getId()));
    }

    @Test
    void writesRequireUserId() throws Exception {
        mvc().perform(put("/internal/reminders/update/1").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment\":\"Updated\"}"))
                .andExpect(status().isBadRequest());
        mvc().perform(delete("/internal/reminders/delete").param("id", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void unknownReminderReturnsNotFoundForWrites() throws Exception {
        mvc().perform(put("/internal/reminders/update/999999").param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"comment\":\"Updated\"}"))
                .andExpect(status().isNotFound());
        mvc().perform(delete("/internal/reminders/delete").param("id", "999999").param("userId", "1"))
                .andExpect(status().isNotFound());
    }

    private MockMvc mvc() {
        return MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    private Reminder createReminder(Application application) {
        Reminder reminder = new Reminder();
        reminder.setApplication(application);
        reminder.setReminderDate(LocalDateTime.of(2026, 11, 5, 9, 0));
        reminder.setComment("Original");
        return reminderRepository.saveAndFlush(reminder);
    }

    private Application createApplication(String email) {
        User user = userRepository.save(new User("Alice", "Martin", email, "hash"));
        Company company = companyRepository.save(new Company("https://jobscape.test", "JobScape"));
        return applicationRepository.save(
                new Application("Backend Dev", "Paris", "France", "LinkedIn", "url", APPLIED, "remote", user, company)
        );
    }
}
