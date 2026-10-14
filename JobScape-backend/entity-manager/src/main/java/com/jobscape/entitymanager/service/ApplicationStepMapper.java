package com.jobscape.entitymanager.service;

import com.jobscape.entitymanager.dto.ApplicationStepDto;
import com.jobscape.entitymanager.model.Application;
import com.jobscape.entitymanager.model.ApplicationStep;
import com.jobscape.entitymanager.model.Contact;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStepMapper {

    public ApplicationStepDto toDto(ApplicationStep step) {
        ApplicationStepDto dto = new ApplicationStepDto();

        dto.setId(step.getId());
        dto.setComment(step.getComment());
        dto.setStepDate(step.getStepDate());
        dto.setStepStatus(step.getStepStatus());
        dto.setCreatedAt(step.getCreatedAt());
        dto.setUpdatedAt(step.getUpdatedAt());


        dto.setContactId(
                step.getContact() == null
                        ? null
                        : step.getContact().getId()
        );

        return dto;
    }

    public ApplicationStep toEntity(
            ApplicationStepDto dto,
            Application application,
            Contact contact
    ) {
        ApplicationStep step = new ApplicationStep();

        step.setComment(dto.getComment());
        step.setStepDate(dto.getStepDate());
        step.setStepStatus(dto.getStepStatus());
        step.setApplication(application);
        step.setContact(contact);

        return step;
    }
}