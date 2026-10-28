package com.jobscape.entitymanager.model;

import com.jobscape.entitymanager.repository.ApplicationRepository;
import com.jobscape.entitymanager.repository.ApplicationStepRepository;
import com.jobscape.entitymanager.repository.CompanyRepository;
import com.jobscape.entitymanager.repository.ContactRepository;
import com.jobscape.entitymanager.repository.ReminderRepository;
import com.jobscape.entitymanager.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.jobscape.entitymanager.model.ApplicationStatus.APPLIED;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:entity-lifecycle;MODE=MySQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false",
        "spring.jpa.open-in-view=false",
        "spring.cloud.consul.enabled=false",
        "spring.cloud.consul.discovery.enabled=false"
})
@Transactional
class EntityLifecycleTest {

    @Autowired UserRepository userRepository;
    @Autowired CompanyRepository companyRepository;
    @Autowired ContactRepository contactRepository;
    @Autowired ApplicationRepository applicationRepository;
    @Autowired ApplicationStepRepository stepRepository;
    @Autowired ReminderRepository reminderRepository;
    @Autowired EntityManager entityManager;

    @Test
    void savesCompanyWithoutDomain() {
        Company company = new Company();
        company.setName("New startup");

        Company saved = companyRepository.saveAndFlush(company);

        entityManager.clear();
        assertNull(companyRepository.findById(saved.getId()).orElseThrow().getDomain());
    }

    @Test
    void deletingApplicationDeletesStepsAndReminders() {
        Graph graph = persistGraph();

        applicationRepository.deleteById(graph.applicationId());
        applicationRepository.flush();

        assertFalse(stepRepository.existsById(graph.stepId()));
        assertFalse(reminderRepository.existsById(graph.reminderId()));
    }

    @Test
    void deletingContactKeepsStepAndClearsContactReference() {
        Graph graph = persistGraph();
        entityManager.clear();

        contactRepository.deleteById(graph.contactId());
        contactRepository.flush();
        entityManager.clear();

        assertNull(stepRepository.findById(graph.stepId()).orElseThrow().getContact());
    }

    @Test
    void deletingReferencedCompanyIsRejected() {
        Graph graph = persistGraph();
        entityManager.clear();

        assertThrows(RuntimeException.class, () -> {
            companyRepository.deleteById(graph.companyId());
            companyRepository.flush();
        });
    }

    @Test
    void deletingUserDeletesOwnedJobSearchData() {
        Graph graph = persistGraph();

        userRepository.deleteById(graph.userId());
        userRepository.flush();

        assertFalse(applicationRepository.existsById(graph.applicationId()));
        assertFalse(contactRepository.existsById(graph.contactId()));
        assertFalse(stepRepository.existsById(graph.stepId()));
        assertFalse(reminderRepository.existsById(graph.reminderId()));
    }

    private Graph persistGraph() {
        User user = new User("Alice", "Martin", "alice-" + System.nanoTime() + "@example.com", "hash");
        Company company = new Company("example.com", "Example");
        userRepository.save(user);
        companyRepository.save(company);

        Contact contact = new Contact();
        contact.setName("Recruiter");
        contact.setUser(user);
        contact.setCompany(company);
        contactRepository.save(contact);

        Application application = new Application(
                "Engineer", "Paris", "France", "Direct", "https://example.com/job",
                APPLIED, "REMOTE", user, company
        );

        ApplicationStep step = new ApplicationStep();
        step.setApplication(application);
        step.setContact(contact);
        step.setStepStatus(APPLIED);
        step.setStepDate(LocalDateTime.now());
        application.getSteps().add(step);

        Reminder reminder = new Reminder();
        reminder.setApplication(application);
        reminder.setReminderDate(LocalDateTime.now().plusDays(1));

        applicationRepository.saveAndFlush(application);
        reminderRepository.saveAndFlush(reminder);

        return new Graph(
                user.getId(), company.getId(), contact.getId(), application.getId(), step.getId(), reminder.getId()
        );
    }

    private record Graph(
            Long userId,
            Long companyId,
            Long contactId,
            Long applicationId,
            Long stepId,
            Long reminderId
    ) {}
}
