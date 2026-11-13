package com.jobscape.applicationservice.service;

import com.jobscape.applicationservice.client.ApplicationClient;
import com.jobscape.applicationservice.dto.application.ApplicationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ApplicationOwnershipService {
    private final ApplicationClient applicationClient;

    public ApplicationDto requireOwnedApplication(Long applicationId, Long userId) {
        requireAuthenticatedUser(userId);
        if (applicationId == null || applicationId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "applicationId must be positive");
        }

        ApplicationDto application = applicationClient.getApplicationById(applicationId);
        if (!Objects.equals(application.getUserId(), userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found");
        }
        return application;
    }

    public void requireAuthenticatedUser(Long userId) {
        if (userId == null || userId <= 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found");
        }
    }
}
