package com.jobscape.applicationservice.service;

import com.jobscape.applicationservice.client.ApplicationClient;
import com.jobscape.applicationservice.client.ApplicationStepClient;
import com.jobscape.applicationservice.client.ReminderClient;
import com.jobscape.applicationservice.dto.*;
import com.jobscape.applicationservice.model.ApplicationCategory;
import com.jobscape.applicationservice.model.ApplicationStatus;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jobscape.applicationservice.model.ApplicationStatus.*;

@Service
@RequiredArgsConstructor

public class ApplicationService {

    private final ApplicationClient applicationClient;
    private final ApplicationStepClient applicationStepClient;
    private final ReminderClient reminderClient;

    public ApplicationDto create(CreateApplicationRequest request) {
        ApplicationDto application = new ApplicationDto();
        ApplicationStepDto applicationStep = new ApplicationStepDto();

        application.setJobTitle(request.getJobTitle());
        application.setCity(request.getCity());
        application.setCountry(request.getCountry());
        application.setSource(request.getSource());
        application.setUrl(request.getUrl());
        application.setStatus(request.getInitialStatus());
        application.setWorkMode(request.getWorkMode());
        application.setUserId(request.getUserId());


        applicationStep.setComment(request.getComment());
        applicationStep.setStepStatus(request.getInitialStatus());
        if (request.getInitialDate() != null) {
            applicationStep.setStepDate(request.getInitialDate().atStartOfDay());
        }

        ApplicationDto createdApplication = applicationClient.create(application);
        applicationStep.setApplicationId(createdApplication.getId());
        applicationStepClient.create(applicationStep);

        if (request.isCreateReminder()) {
            CreateReminderRequest reminder = new CreateReminderRequest();
            reminder.setApplicationId(createdApplication.getId());
            reminder.setApplicationDate(request.getInitialDate() != null
                    ? request.getInitialDate()
                    : createdApplication.getCreatedAt().toLocalDate());
            reminder.setComment("Relancer l'entreprise");
            reminderClient.create(reminder);
        }

        return createdApplication;
    }

    public ApplicationsByStatusResponse getApplicationsByStatus(Long userId) {
        List<ApplicationDto> applications = applicationClient.getApplicationByUserId(userId);

        Map<ApplicationCategory, List<ApplicationDto>> applicationsByStatus = new HashMap<>();

        ApplicationsByStatusResponse response = new ApplicationsByStatusResponse();


        for (ApplicationCategory applicationCategory : ApplicationCategory.values()) {
            applicationsByStatus.put(applicationCategory, new ArrayList<>());
        }
            for (ApplicationDto application : applications) {
                ApplicationCategory category = switch (application.getStatus()) {
                    case WISHLIST ->
                            ApplicationCategory.WISHLIST;

                    case APPLIED, FOLLOWED_UP ->
                            ApplicationCategory.APPLIED;

                    case FIRST_CONTACT, HR_INTERVIEW, MANAGER_INTERVIEW, TECHNICAL_TEST ->
                            ApplicationCategory.INTERVIEW;

                    case OFFER_RECEIVED ->
                            ApplicationCategory.OFFER;

                    case REJECTED ->
                            ApplicationCategory.REJECTED;
                };
               applicationsByStatus.get(category).add(application);
            }


        response.setApplicationsByStatus(applicationsByStatus);

        return response;
    }

    public ApplicationDto getApplicationById(Long id) {
        return applicationClient.getApplicationById(id);
    }

    public void deleteApplicationById(Long id) {
        applicationClient.delete(id);
    }



}
