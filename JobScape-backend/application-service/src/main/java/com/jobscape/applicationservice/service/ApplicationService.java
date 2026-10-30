package com.jobscape.applicationservice.service;

import com.jobscape.applicationservice.client.*;
import com.jobscape.applicationservice.dto.application.ApplicationDetailDto;
import com.jobscape.applicationservice.dto.application.ApplicationDto;
import com.jobscape.applicationservice.dto.application.ApplicationsByStatusResponse;
import com.jobscape.applicationservice.dto.application.CreateApplicationRequest;
import com.jobscape.applicationservice.dto.applicationstep.ApplicationStepDto;
import com.jobscape.applicationservice.dto.company.CompanyDto;
import com.jobscape.applicationservice.dto.company.CompanySelectionDto;
import com.jobscape.applicationservice.dto.contact.ContactSummaryDto;
import com.jobscape.applicationservice.dto.reminder.CreateReminderRequest;
import com.jobscape.applicationservice.dto.reminder.ReminderDto;
import com.jobscape.applicationservice.model.ApplicationCategory;
import com.jobscape.applicationservice.service.mapper.ApplicationMapper;
import com.jobscape.applicationservice.service.mapper.ApplicationStepMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor

public class ApplicationService {

    private final ApplicationClient applicationClient;
    private final ApplicationStepClient applicationStepClient;
    private final ReminderClient reminderClient;
    private final CompanyClient companyClient;
    private final ContactClient contactClient;
    private final ApplicationMapper applicationMapper;
    private final ApplicationStepMapper applicationStepMapper;


    public ApplicationDto create(CreateApplicationRequest request) {

        CompanySelectionDto companySelectionDto = new CompanySelectionDto();
        companySelectionDto.setName(request.getCompanyName());
        companySelectionDto.setDomain(request.getCompanyDomain());
        companySelectionDto.setLogoUrl(request.getCompanyLogoUrl());

        CompanyDto company = companyClient.resolveCompany(companySelectionDto);
        ApplicationDto application = applicationMapper.toDto(request);
        application.setCompanyId(company.getId());
        ApplicationDto createdApplication = applicationClient.create(application);
        ApplicationStepDto applicationStep = applicationStepMapper.toInitialDto(request, createdApplication);
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

    public ApplicationDetailDto getApplicationDetail(Long applicationId, Long userId) {
        ApplicationDto application = applicationClient.getApplicationById(applicationId);
        if (userId == null || !Objects.equals(userId, application.getUserId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found");
        }
        List<ContactSummaryDto> contacts = contactClient.getContactsByCompanyId(application.getCompanyId(), userId);
        List<ReminderDto> reminders = reminderClient.getRemindersByApplicationId(applicationId);

        ApplicationDetailDto applicationDetailDto = new ApplicationDetailDto();
        applicationDetailDto.setApplication(application);
        applicationDetailDto.setContacts(contacts);
        applicationDetailDto.setReminders(reminders);

        return applicationDetailDto;

    }


    public ApplicationDto getApplicationById(Long id) {
        return applicationClient.getApplicationById(id);
    }

    public void deleteApplicationById(Long id) {
        applicationClient.delete(id);
    }



}
