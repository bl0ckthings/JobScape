package com.jobscape.entitymanager.controller;

import com.jobscape.entitymanager.model.Application;
import com.jobscape.entitymanager.model.ApplicationStatus;
import com.jobscape.entitymanager.model.ApplicationStep;
import com.jobscape.entitymanager.model.Company;
import com.jobscape.entitymanager.model.Contact;
import com.jobscape.entitymanager.model.Reminder;
import com.jobscape.entitymanager.model.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class EntityManagerControllerIntegrationTest {
    @Autowired private MockMvc mvc;
    @Autowired private EntityManager entityManager;
    @Autowired private ObjectMapper json;

    private User alice;
    private User bob;
    private Company company;
    private Company otherCompany;
    private Application aliceApplication;
    private Application bobApplication;
    private Contact contact;
    private ApplicationStep initialStep;
    private Reminder initialReminder;

    @BeforeEach
    void setUpTwoUsersWithOneApplicationEach() {
        alice = new User();
        alice.setFirstName("Alice");
        alice.setLastName("Martin");
        alice.setEmail("alice@example.test");
        alice.setPassword("hash");
        entityManager.persist(alice);

        bob = new User();
        bob.setFirstName("Bob");
        bob.setLastName("Durand");
        bob.setEmail("bob@example.test");
        bob.setPassword("hash");
        entityManager.persist(bob);

        company = new Company();
        company.setName("Acme");
        company.setDomain("acme.test");
        company.setWebsite("https://acme.test");
        entityManager.persist(company);

        otherCompany = new Company();
        otherCompany.setName("Autre entreprise");
        otherCompany.setDomain("other.test");
        otherCompany.setWebsite("https://other.test");
        entityManager.persist(otherCompany);

        aliceApplication = new Application();
        aliceApplication.setJobTitle("Développeur Java");
        aliceApplication.setStatus(ApplicationStatus.APPLIED);
        aliceApplication.setUser(alice);
        aliceApplication.setCompany(company);
        entityManager.persist(aliceApplication);

        bobApplication = new Application();
        bobApplication.setJobTitle("Designer");
        bobApplication.setStatus(ApplicationStatus.APPLIED);
        bobApplication.setUser(bob);
        bobApplication.setCompany(company);
        entityManager.persist(bobApplication);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void createsAndUpdatesApplicationUsingOnlyRelationshipIds() throws Exception {
        long id = create("/internal/applications/create", """
                {"jobTitle":"Stage Java","city":"Paris","country":"France","source":"LinkedIn",
                 "url":"https://job.test","status":"APPLIED","workMode":"REMOTE","userId":%d,"companyId":%d}
                """.formatted(alice.getId(), company.getId()));
        mvc.perform(put("/internal/applications/update").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"id":%d,"jobTitle":"CDI Java","city":"Marseille","country":"Belgique","source":"Indeed",
                         "url":"https://new.test","status":"HR_INTERVIEW","workMode":"HYBRID","userId":%d,"companyId":%d}
                        """.formatted(id, alice.getId(), otherCompany.getId())))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("HR_INTERVIEW"))
                .andExpect(jsonPath("$.companyId").value(otherCompany.getId().intValue()));
        mvc.perform(get("/internal/applications/{id}", id))
                .andExpect(jsonPath("$.jobTitle").value("CDI Java"))
                .andExpect(jsonPath("$.companyName").doesNotExist());
        mvc.perform(get("/internal/applications/user/{id}", alice.getId()))
                .andExpect(jsonPath("$.length()").value(2));
        mvc.perform(get("/internal/applications")).andExpect(jsonPath("$.length()").value(3));
        mvc.perform(delete("/internal/applications/delete/{id}", id)).andExpect(status().isOk());
    }

    @Test
    void createsAndUpdatesContactDetails() throws Exception {
        long id = create("/internal/contact/create", """
                {"name":"RH","phone":"0102030405","email":"rh@acme.test","role":"Recruteur",
                 "company":{"id":%d},"userId":%d}
                """.formatted(company.getId(), alice.getId()));
        mvc.perform(get("/internal/contact/{id}", id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.company.name").value("Acme"));
        mvc.perform(put("/internal/contact/update/{id}", id).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name":"Nouvelle RH","phone":"0607080910","email":"new@acme.test","role":"Manager",
                         "company":{"id":%d},"userId":%d}
                        """.formatted(otherCompany.getId(), alice.getId())))
                .andExpect(status().isOk()).andExpect(jsonPath("$.company.id").value(otherCompany.getId().intValue()));
        mvc.perform(get("/internal/contact/user/{id}", alice.getId())).andExpect(jsonPath("$.length()").value(1));
        mvc.perform(get("/internal/contact")).andExpect(jsonPath("$[0].role").value("Manager"));
        entityManager.flush();
        entityManager.clear();
        mvc.perform(get("/internal/companies/{id}", otherCompany.getId()))
                .andExpect(jsonPath("$.contacts[0].name").value("Nouvelle RH"));
        mvc.perform(delete("/internal/contact/delete/{id}", id)).andExpect(status().isOk());
    }

    @Test
    void filtersContactsByCompanyAndOwnerAndSortsByDateAndId() throws Exception {
        Contact recent = createContact("Plus récent", company, alice, LocalDateTime.of(2026, 11, 3, 10, 0));
        Contact firstOlderContact = createContact("Ancien premier", company, alice, LocalDateTime.of(2026, 11, 1, 10, 0));
        Contact secondOlderContact = createContact("Ancien deuxième", company, alice, LocalDateTime.of(2026, 11, 1, 10, 0));
        createContact("Autre utilisateur", company, bob, LocalDateTime.of(2026, 11, 1, 10, 0));
        createContact("Autre entreprise", otherCompany, alice, LocalDateTime.of(2026, 11, 1, 10, 0));
        entityManager.flush();
        entityManager.clear();

        mvc.perform(get("/internal/contact/company/{id}", company.getId()).param("userId", alice.getId().toString()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(firstOlderContact.getId().intValue()))
                .andExpect(jsonPath("$[1].id").value(secondOlderContact.getId().intValue()))
                .andExpect(jsonPath("$[2].id").value(recent.getId().intValue()));
    }

    @Test
    void createsAndUpdatesStepWithContact() throws Exception {
        seedHistory();
        long id = create("/internal/steps/create", """
                {"applicationId":%d,"stepStatus":"APPLIED","stepDate":"2026-11-03T10:00:00","comment":"CV envoyé"}
                """.formatted(aliceApplication.getId()));
        mvc.perform(put("/internal/steps/update/{id}", id).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"applicationId":%d,"contactId":%d,"stepStatus":"HR_INTERVIEW",
                         "stepDate":"2026-11-04T14:00:00","comment":"Entretien"}
                        """.formatted(aliceApplication.getId(), contact.getId())))
                .andExpect(status().isOk()).andExpect(jsonPath("$.contactId").value(contact.getId().intValue()));
        mvc.perform(get("/internal/steps/{id}", id))
                .andExpect(jsonPath("$.stepStatus").value("HR_INTERVIEW"));
        mvc.perform(get("/internal/applications/{id}", aliceApplication.getId()))
                .andExpect(jsonPath("$.steps.length()").value(2));
        mvc.perform(delete("/internal/steps/delete/{id}", id)).andExpect(status().isOk());
    }

    @Test
    void sortsStepsFromNewestToOldestAndBreaksTiesById() throws Exception {
        ApplicationStep olderStep = createStep(aliceApplication, ApplicationStatus.APPLIED, LocalDateTime.of(2026, 11, 1, 10, 0));
        ApplicationStep newerStep = createStep(aliceApplication, ApplicationStatus.FIRST_CONTACT, LocalDateTime.of(2026, 11, 3, 10, 0));
        ApplicationStep newerStepWithHigherId = createStep(aliceApplication, ApplicationStatus.HR_INTERVIEW, LocalDateTime.of(2026, 11, 3, 10, 0));
        createStep(bobApplication, ApplicationStatus.APPLIED, LocalDateTime.of(2026, 11, 4, 10, 0));
        entityManager.flush();
        entityManager.clear();

        mvc.perform(get("/internal/steps/application/{id}", aliceApplication.getId()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(newerStepWithHigherId.getId().intValue()))
                .andExpect(jsonPath("$[1].id").value(newerStep.getId().intValue()))
                .andExpect(jsonPath("$[2].id").value(olderStep.getId().intValue()));
    }

    @Test
    void createsReminderAndPreservesOmittedFieldsDuringPartialUpdate() throws Exception {
        long id = create("/internal/reminders/create", """
                {"applicationId":%d,"reminderDate":"2026-11-10T09:00:00","comment":"Relancer"}
                """.formatted(aliceApplication.getId()));
        mvc.perform(put("/internal/reminders/update/{id}", id).param("userId", alice.getId().toString())
                .contentType(MediaType.APPLICATION_JSON).content("""
                        {"applicationId":%d,"comment":"Relance modifiée"}
                        """.formatted(bobApplication.getId())))
                .andExpect(status().isOk()).andExpect(jsonPath("$.applicationId").value(aliceApplication.getId().intValue()))
                .andExpect(jsonPath("$.reminderDate").value("2026-11-10T09:00:00"));
        mvc.perform(put("/internal/reminders/update/{id}", id).param("userId", alice.getId().toString())
                .contentType(MediaType.APPLICATION_JSON).content("""
                        {"reminderDate":"2026-11-11T09:00:00"}
                        """))
                .andExpect(status().isOk()).andExpect(jsonPath("$.comment").value("Relance modifiée"));
        mvc.perform(get("/internal/reminders/{id}", id))
                .andExpect(jsonPath("$.reminderDate").value("2026-11-11T09:00:00"));
        mvc.perform(get("/internal/reminders")).andExpect(jsonPath("$.length()").value(1));
        mvc.perform(delete("/internal/reminders/delete").param("id", String.valueOf(id)).param("userId", alice.getId().toString()))
                .andExpect(status().isOk());
        assertNull(entityManager.find(Reminder.class, id));
    }

    @Test
    void rejectsUpdatingAndDeletingAnotherUsersReminder() throws Exception {
        seedHistory();
        mvc.perform(put("/internal/reminders/update/{id}", initialReminder.getId()).param("userId", bob.getId().toString())
                .contentType(MediaType.APPLICATION_JSON).content("{\"comment\":\"Intrusion\"}"))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"));
        mvc.perform(delete("/internal/reminders/delete").param("id", initialReminder.getId().toString()).param("userId", bob.getId().toString()))
                .andExpect(status().isNotFound());
        assertEquals("Relancer", entityManager.find(Reminder.class, initialReminder.getId()).getComment());
    }

    @Test
    void sortsRemindersByDateAndIdWithoutMixingApplications() throws Exception {
        Reminder laterReminder = createReminder(aliceApplication, LocalDateTime.of(2026, 11, 10, 9, 0));
        Reminder firstReminder = createReminder(aliceApplication, LocalDateTime.of(2026, 11, 8, 9, 0));
        Reminder secondReminder = createReminder(aliceApplication, LocalDateTime.of(2026, 11, 8, 9, 0));
        createReminder(bobApplication, LocalDateTime.of(2026, 11, 7, 9, 0));
        entityManager.flush();
        entityManager.clear();

        mvc.perform(get("/internal/reminders/applications/{id}", aliceApplication.getId()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(firstReminder.getId().intValue()))
                .andExpect(jsonPath("$[1].id").value(secondReminder.getId().intValue()))
                .andExpect(jsonPath("$[2].id").value(laterReminder.getId().intValue()));
    }

    @Test
    void deletesApplicationWithItsStepsAndReminders() throws Exception {
        seedHistory();
        mvc.perform(delete("/internal/applications/delete/{id}", aliceApplication.getId())).andExpect(status().isOk());
        entityManager.flush();
        entityManager.clear();
        assertNull(entityManager.find(ApplicationStep.class, initialStep.getId()));
        assertNull(entityManager.find(Reminder.class, initialReminder.getId()));
        assertNotNull(entityManager.find(Application.class, bobApplication.getId()));
    }

    @Test
    void deletesContactWithoutDeletingStep() throws Exception {
        seedHistory();
        mvc.perform(delete("/internal/contact/delete/{id}", contact.getId())).andExpect(status().isOk());
        entityManager.flush();
        entityManager.clear();
        mvc.perform(get("/internal/steps/{id}", initialStep.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$.contactId").isEmpty());
    }

    @Test
    void deletesUserWithTheirJobSearchData() throws Exception {
        seedHistory();
        mvc.perform(delete("/internal/users/delete/{id}", alice.getId())).andExpect(status().isOk());
        entityManager.flush();
        entityManager.clear();
        assertNull(entityManager.find(Application.class, aliceApplication.getId()));
        assertNull(entityManager.find(Contact.class, contact.getId()));
        assertNull(entityManager.find(ApplicationStep.class, initialStep.getId()));
        assertNull(entityManager.find(Reminder.class, initialReminder.getId()));
        assertNotNull(entityManager.find(User.class, bob.getId()));
    }

    @Test
    void rejectsDeletingReferencedCompany() {
        // The constraint is checked on flush.
        Exception error = assertThrows(Exception.class, () -> {
            mvc.perform(delete("/internal/companies/delete/{id}", company.getId()));
            entityManager.flush();
        });
        Throwable cause = error;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        SQLException sqlError = assertInstanceOf(SQLException.class, cause);
        assertEquals("23503", sqlError.getSQLState());
    }

    @Test
    void returnsReadableNotFoundErrorForMissingResource() throws Exception {
        mvc.perform(get("/internal/reminders/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.status").value(404));
    }

    private long create(String path, String body) throws Exception {
        String response = mvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        return json.readTree(response).get("id").asLong();
    }

    private Contact createContact(String name, Company company, User user, LocalDateTime createdAt) {
        Contact contact = new Contact();
        contact.setName(name);
        contact.setCompany(company);
        contact.setUser(user);
        entityManager.persist(contact);
        // Set the scenario date after JPA auditing.
        contact.setCreatedAt(createdAt);
        return contact;
    }

    private ApplicationStep createStep(Application application, ApplicationStatus status, LocalDateTime date) {
        ApplicationStep step = new ApplicationStep();
        step.setApplication(application);
        step.setStepStatus(status);
        step.setStepDate(date);
        entityManager.persist(step);
        return step;
    }

    private Reminder createReminder(Application application, LocalDateTime date) {
        Reminder reminder = new Reminder();
        reminder.setApplication(application);
        reminder.setReminderDate(date);
        entityManager.persist(reminder);
        return reminder;
    }

    private void seedHistory() {
        contact = createContact("RH", company, alice, LocalDateTime.of(2026, 11, 3, 10, 0));
        initialStep = createStep(aliceApplication, ApplicationStatus.APPLIED, LocalDateTime.of(2026, 11, 3, 10, 0));
        initialStep.setContact(contact);
        initialReminder = createReminder(aliceApplication, LocalDateTime.of(2026, 11, 10, 9, 0));
        initialReminder.setComment("Relancer");
        entityManager.flush();
        entityManager.clear();
    }
}
