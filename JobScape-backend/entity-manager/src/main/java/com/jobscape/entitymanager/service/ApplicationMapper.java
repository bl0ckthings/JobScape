package com.jobscape.entitymanager.service;

import com.jobscape.entitymanager.dto.ApplicationDto;
import com.jobscape.entitymanager.dto.ApplicationStepDto;
import com.jobscape.entitymanager.dto.ReminderDto;
import com.jobscape.entitymanager.model.Application;
import com.jobscape.entitymanager.model.Reminder;
import com.jobscape.entitymanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ApplicationMapper {
    // Entity -> DTO: outgoing response
    public ApplicationDto toDto(Application application) {
        ApplicationDto dto = new ApplicationDto();

        dto.setId(application.getId());
        dto.setJobTitle(application.getJobTitle());
        dto.setCity(application.getCity());
        dto.setCountry(application.getCountry());
        dto.setSource(application.getSource());
        dto.setUrl(application.getUrl());
        dto.setStatus(application.getStatus());
        dto.setWorkMode(application.getWorkMode());
        dto.setCreatedAt(application.getCreatedAt());
        dto.setUpdatedAt(application.getUpdatedAt());
        dto.setUserId(application.getUser().getId());
        dto.setCompanyId(application.getCompany().getId());
        dto.setSteps();

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