package com.jobscape.entitymanager.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class EntityLombokSafetyTest {

    @Test
    void objectMethodsDoNotTraverseEntityRelationships() {
        Application application = new Application();
        ApplicationStep step = new ApplicationStep();

        application.getSteps().add(step);
        step.setApplication(application);

        assertDoesNotThrow(application::toString);
    }

    @Test
    void userToStringDoesNotExposePassword() {
        User user = new User("Alice", "Martin", "alice@example.com", "secret-password");

        assertFalse(user.toString().contains("secret-password"));
    }

    @Test
    void entityGettersAndSettersRemainAvailable() {
        Company company = new Company();
        Contact contact = new Contact();
        Reminder reminder = new Reminder();

        company.setName("Acme");
        contact.setCompany(company);
        reminder.setComment("Follow up");

        assertEquals("Acme", company.getName());
        assertEquals(company, contact.getCompany());
        assertEquals("Follow up", reminder.getComment());
    }
}
