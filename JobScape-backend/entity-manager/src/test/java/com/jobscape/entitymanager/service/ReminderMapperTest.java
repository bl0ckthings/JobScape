package com.jobscape.entitymanager.service;

import com.jobscape.entitymanager.dto.ReminderDto;
import com.jobscape.entitymanager.model.Application;
import com.jobscape.entitymanager.model.Reminder;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ReminderMapperTest {

    private final ReminderMapper mapper = new ReminderMapper();

    @Test
    void mapsReminderToDto() {
        LocalDateTime reminderDate = LocalDateTime.of(2026, 10, 20, 9, 0);
        Application application = new Application();
        application.setId(4L);

        Reminder reminder = new Reminder();
        reminder.setId(1L);
        reminder.setReminderDate(reminderDate);
        reminder.setComment("Follow up");
        reminder.setApplication(application);

        ReminderDto dto = mapper.toDto(reminder);

        assertEquals(1L, dto.getId());
        assertEquals(reminderDate, dto.getReminderDate());
        assertEquals("Follow up", dto.getComment());
        assertEquals(4L, dto.getApplicationId());
    }

    @Test
    void mapsReminderDtoToEntityUsingResolvedApplication() {
        ReminderDto dto = new ReminderDto();
        dto.setReminderDate(LocalDateTime.of(2026, 10, 20, 9, 0));
        dto.setComment("Follow up");
        Application application = new Application();

        Reminder reminder = mapper.toEntity(dto, application);

        assertEquals(dto.getReminderDate(), reminder.getReminderDate());
        assertEquals("Follow up", reminder.getComment());
        assertSame(application, reminder.getApplication());
    }
}
