package com.jobscape.applicationservice.service;

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
    private final ContactClient contactClient;
    private final ApplicationOwnershipService applicationOwnershipService;

    public List<ApplicationStepDto> getByApplicationId(Long applicationId, Long userId) {
        applicationOwnershipService.requireOwnedApplication(applicationId, userId);
        return applicationStepClient.getByApplicationId(applicationId);
    }

    public ApplicationStepDto getStepById(Long stepId, Long userId) {
        applicationOwnershipService.requireAuthenticatedUser(userId);
        if (stepId == null || stepId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "stepId must be positive");
        }
        ApplicationStepDto step = applicationStepClient.getById(stepId);
        applicationOwnershipService.requireOwnedApplication(step.getApplicationId(), userId);
        return step;
    }
    public ApplicationStepDto createStep(
            Long applicationId,
            Long userId,
            CreateApplicationStepRequest request
    ) {
        validateRequest(request);

        ApplicationDto application = applicationOwnershipService
                .requireOwnedApplication(applicationId, userId);
        validateContact(request.getContactId(), application, userId);

        ApplicationStepDto step = new ApplicationStepDto();
        step.setApplicationId(applicationId);
        step.setStepStatus(request.getStepStatus());
        step.setStepDate(request.getStepDate());
        step.setComment(request.getComment());
        step.setContactId(request.getContactId());
        return applicationStepClient.create(step);
    }

    public ApplicationStepDto updateStep(Long stepId, Long userId, CreateApplicationStepRequest request) {
        if (stepId == null || stepId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "stepId must be positive");
        }
        validateRequest(request);

        ApplicationStepDto step = applicationStepClient.getById(stepId);
        ApplicationDto application = applicationOwnershipService
                .requireOwnedApplication(step.getApplicationId(), userId);
        validateContact(request.getContactId(), application, userId);

        step.setComment(request.getComment());
        step.setStepDate(request.getStepDate());
        step.setStepStatus(request.getStepStatus());
        step.setApplicationId(application.getId());
        step.setContactId(request.getContactId());
        return applicationStepClient.update(stepId, step);
    }

    public void deleteStep(Long stepId) {
        applicationStepClient.delete(stepId);
    }

    private void validateRequest(CreateApplicationStepRequest request) {
        if (request == null || request.getStepStatus() == null || request.getStepDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "stepStatus and stepDate are required");
        }
        if (request.getContactId() != null && request.getContactId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "contactId must be positive");
        }
    }

    private void validateContact(Long contactId, ApplicationDto application, Long userId) {
        if (contactId == null) {
            return;
        }
        boolean contactAllowed = contactClient.getContactsByCompanyId(application.getCompanyId(), userId)
                .stream()
                .anyMatch(contact -> Objects.equals(contact.getId(), contactId));
        if (!contactAllowed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found");
        }
    }
}
