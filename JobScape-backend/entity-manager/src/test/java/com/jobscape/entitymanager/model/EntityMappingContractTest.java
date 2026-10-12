package com.jobscape.entitymanager.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.junit.jupiter.api.Test;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityMappingContractTest {

    @Test
    void requiredFieldsAreNotNullable() throws Exception {
        assertFalse(column(User.class, "email").nullable());
        assertFalse(column(User.class, "password").nullable());
        assertFalse(column(Company.class, "name").nullable());
        assertFalse(column(Application.class, "jobTitle").nullable());
        assertFalse(joinColumn(ApplicationStep.class, "application").nullable());
        assertFalse(column(Reminder.class, "reminderDate").nullable());
    }

    @Test
    void commentsUseTextColumns() throws Exception {
        assertEquals("TEXT", column(ApplicationStep.class, "comment").columnDefinition());
        assertEquals("TEXT", column(Reminder.class, "comment").columnDefinition());
    }

    @Test
    void queryIndexesAreDeclared() {
        assertIndexes(Application.class,
                "user_id,status",
                "user_id,created_at");
        assertIndexes(ApplicationStep.class, "application_id,step_date");
        assertIndexes(Reminder.class, "application_id,reminder_date");
        assertIndexes(Contact.class,
                "company_id,name",
                "user_id,company_id");
    }

    @Test
    void mutableEntitiesUseJpaAuditing() throws Exception {
        for (Class<?> type : List.of(
                User.class,
                Company.class,
                Contact.class,
                Application.class,
                ApplicationStep.class,
                Reminder.class
        )) {
            EntityListeners listeners = type.getAnnotation(EntityListeners.class);
            assertNotNull(listeners, type.getSimpleName());
            assertTrue(Arrays.asList(listeners.value()).contains(AuditingEntityListener.class), type.getSimpleName());
            assertNotNull(field(type, "createdAt").getAnnotation(CreatedDate.class), type.getSimpleName());
            assertNotNull(field(type, "updatedAt").getAnnotation(LastModifiedDate.class), type.getSimpleName());
        }
    }

    @Test
    void databaseDeleteActionsMatchDomainRules() throws Exception {
        assertDeleteAction(Application.class, "user", OnDeleteAction.CASCADE);
        assertDeleteAction(Contact.class, "user", OnDeleteAction.CASCADE);
        assertDeleteAction(ApplicationStep.class, "application", OnDeleteAction.CASCADE);
        assertDeleteAction(Reminder.class, "application", OnDeleteAction.CASCADE);
        assertDeleteAction(ApplicationStep.class, "contact", OnDeleteAction.SET_NULL);
        assertDeleteAction(Application.class, "company", OnDeleteAction.RESTRICT);
        assertDeleteAction(Contact.class, "company", OnDeleteAction.RESTRICT);
    }

    private static Column column(Class<?> type, String fieldName) throws Exception {
        Column annotation = field(type, fieldName).getAnnotation(Column.class);
        assertNotNull(annotation, type.getSimpleName() + "." + fieldName);
        return annotation;
    }

    private static JoinColumn joinColumn(Class<?> type, String fieldName) throws Exception {
        JoinColumn annotation = field(type, fieldName).getAnnotation(JoinColumn.class);
        assertNotNull(annotation, type.getSimpleName() + "." + fieldName);
        return annotation;
    }

    private static Field field(Class<?> type, String fieldName) throws Exception {
        return type.getDeclaredField(fieldName);
    }

    private static void assertIndexes(Class<?> type, String... expectedColumns) {
        Table table = type.getAnnotation(Table.class);
        List<String> actualColumns = Arrays.stream(table.indexes())
                .map(Index::columnList)
                .toList();

        for (String expected : expectedColumns) {
            assertTrue(actualColumns.contains(expected), type.getSimpleName() + " missing " + expected);
        }
    }

    private static void assertDeleteAction(
            Class<?> type,
            String fieldName,
            OnDeleteAction expected
    ) throws Exception {
        OnDelete annotation = field(type, fieldName).getAnnotation(OnDelete.class);
        assertNotNull(annotation, type.getSimpleName() + "." + fieldName);
        assertEquals(expected, annotation.action());
    }
}
