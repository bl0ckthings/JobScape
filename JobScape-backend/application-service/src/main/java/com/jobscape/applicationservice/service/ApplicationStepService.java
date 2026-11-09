package com.jobscape.applicationservice.service;

import com.jobscape.applicationservice.client.ApplicationClient;
import com.jobscape.applicationservice.client.ApplicationStepClient;
import com.jobscape.applicationservice.client.ContactClient;
import com.jobscape.applicationservice.dto.application.ApplicationDto;
import com.jobscape.applicationservice.dto.applicationstep.ApplicationStepDto;
import com.jobscape.applicationservice.dto.applicationstep.CreateApplicationStepRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ApplicationStepService {
    private final ApplicationStepClient applicationStepClient;
    private final ApplicationClient applicationClient;
    private final ContactClient contactClient;

    public List<ApplicationStepDto> getByApplicationId(Long applicationId, Long userId) {
        requireOwnedApplication(applicationId, userId);
        return applicationStepClient.getByApplicationId(applicationId);
    }

    public ApplicationStepDto getStepById(Long stepId, Long userId) {
        requireUser(userId);
        if (stepId == null || stepId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "stepId must be positive");
        }
        ApplicationStepDto step = applicationStepClient.getById(stepId);
        requireOwnedApplication(step.getApplicationId(), userId);
        return step;
    }

    public ApplicationStepDto createStep(
            Long applicationId,
            Long userId,
            CreateApplicationStepRequest request
    ) {
        if (request == null || request.getStepStatus() == null || request.getStepDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "stepStatus and stepDate are required");
        }
        if (request.getContactId() != null && request.getContactId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "contactId must be positive");
        }

        ApplicationDto application = requireOwnedApplication(applicationId, userId);
        if (request.getContactId() != null) {
            // Même utilisateur et même entreprise pour le contact.
            boolean contactAllowed = contactClient.getContactsByCompanyId(application.getCompanyId(), userId)
                    .stream()
                    .anyMatch(contact -> Objects.equals(contact.getId(), request.getContactId()));
            if (!contactAllowed) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found");
            }
        }

        ApplicationStepDto step = new ApplicationStepDto();
        step.setApplicationId(applicationId);
        step.setStepStatus(request.getStepStatus());
        step.setStepDate(request.getStepDate());
        step.setComment(request.getComment());
        step.setContactId(request.getContactId());
        return applicationStepClient.create(step);
    }

    private ApplicationDto requireOwnedApplication(Long applicationId, Long userId) {
        requireUser(userId);
        if (applicationId == null || applicationId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "applicationId must be positive");
        }
        ApplicationDto application = applicationClient.getApplicationById(applicationId);
        if (!Objects.equals(application.getUserId(), userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found");
        }
        return application;
    }

    private void requireUser(Long userId) {
        if (userId == null || userId <= 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found");
        }
    }
}
