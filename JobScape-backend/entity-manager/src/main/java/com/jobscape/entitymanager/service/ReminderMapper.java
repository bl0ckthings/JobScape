package com.jobscape.entitymanager.service;

import com.jobscape.entitymanager.dto.ReminderDto;
import com.jobscape.entitymanager.model.Application;
import com.jobscape.entitymanager.model.Reminder;
import org.springframework.stereotype.Component;

@Component
public class ReminderMapper {

    // Entity -> DTO: outgoing response
    public ReminderDto toDto(Reminder reminder) {
        ReminderDto dto = new ReminderDto();

        dto.setId(reminder.getId());
        dto.setReminderDate(reminder.getReminderDate());
        dto.setComment(reminder.getComment());
        dto.setCreatedAt(reminder.getCreatedAt());
        dto.setApplicationId(reminder.getApplication().getId());

        return dto;
    }

    // DTO -> Entity: incoming request
    public Reminder toEntity(
            ReminderDto dto,
            Application application
    ) {
        Reminder reminder = new Reminder();

        reminder.setReminderDate(dto.getReminderDate());
        reminder.setComment(dto.getComment());
        reminder.setApplication(application);

        return reminder;
    }
}