package com.jobscape.applicationservice.controller;

import com.jobscape.applicationservice.client.*;
import com.jobscape.applicationservice.dto.application.ApplicationDto;
import com.jobscape.applicationservice.dto.applicationstep.ApplicationStepDto;
import com.jobscape.applicationservice.dto.company.CompanyDto;
import com.jobscape.applicationservice.dto.contact.ContactSummaryDto;
import com.jobscape.applicationservice.model.ApplicationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"spring.cloud.consul.enabled=false", "spring.cloud.consul.discovery.enabled=false"})
@AutoConfigureMockMvc
class ApplicationControllerIntegrationTest {
    @Autowired private MockMvc mvc;
    @MockitoBean private ApplicationClient applications;
    @MockitoBean private ApplicationStepClient steps;
    @MockitoBean private CompanyClient companies;
    @MockitoBean private ReminderClient reminders;
    @MockitoBean private ContactClient contacts;
    private ApplicationDto application;

    @BeforeEach
    void setUpApplication() {
        application = new ApplicationDto();
        application.setId(42L);
        application.setUserId(7L);
        application.setCompanyId(3L);
        application.setJobTitle("Développeur Java");
        application.setStatus(ApplicationStatus.APPLIED);
        application.setCreatedAt(LocalDateTime.of(2026, 11, 3, 10, 0));
        when(applications.getApplicationById(42L)).thenReturn(application);
        CompanyDto company = new CompanyDto();
        company.setId(3L);
        company.setName("Acme");
        when(companies.getCompanyById(3L)).thenReturn(company);
        when(companies.resolveCompany(any())).thenReturn(company);
    }

    @Test
    void createsApplicationAndInitialStepWithoutOptionalReminder() throws Exception {
        when(applications.create(any())).thenReturn(application);
        mvc.perform(post("/application-service/applications/create")
                        .contentType("application/json").content(creationRequest(false)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.companyName").doesNotExist());
        verify(steps).create(argThat(dto -> Long.valueOf(42).equals(dto.getApplicationId())));
        verifyNoInteractions(reminders);
    }

    @Test
    void usesCreationDateWhenReminderInitialDateIsMissing() throws Exception {
        when(applications.create(any())).thenReturn(application);
        mvc.perform(post("/application-service/applications/create")
                        .contentType("application/json").content(creationRequest(true)))
                .andExpect(status().isOk());
        verify(reminders).create(argThat(dto ->
                java.time.LocalDate.of(2026, 11, 3).equals(dto.getApplicationDate())));
    }

    @Test
    void rejectsInvalidCreationBeforeCallingOtherServices() throws Exception {
        mvc.perform(post("/application-service/applications/create")
                        .contentType("application/json").content("{}"))
                .andExpect(status().isBadRequest());
        verify(companies, never()).resolveCompany(any());
        verify(applications, never()).create(any());
    }

    @Test
    void updatesApplicationUsingIdFromRequestBody() throws Exception {
        when(applications.update(any())).thenReturn(application);

        mvc.perform(put("/application-service/applications/update")
                        .param("userId", "7")
                        .contentType("application/json")
                        .content("""
                                {"id":42,"jobTitle":"Développeur senior"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(42));

        verify(applications).update(argThat(dto -> Long.valueOf(42).equals(dto.getId())));
    }

    @Test
    void returnsApplicationDetailsWithRelatedData() throws Exception {
        when(contacts.getContactsByCompanyId(3L, 7L)).thenReturn(List.of());
        when(reminders.getRemindersByApplicationId(42L)).thenReturn(List.of());
        mvc.perform(get("/application-service/applications/42/detail").param("userId", "7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.company.name").value("Acme"))
                .andExpect(jsonPath("$.application.companyName").doesNotExist())
                .andExpect(jsonPath("$.contacts").isArray());
    }

    @Test
    void addsStepWithAuthorizedContactAndRetrievesIt() throws Exception {
        ContactSummaryDto contact = new ContactSummaryDto();
        contact.setId(12L);
        when(contacts.getContactsByCompanyId(3L, 7L)).thenReturn(List.of(contact));
        ApplicationStepDto step = new ApplicationStepDto();
        step.setId(900L);
        step.setApplicationId(42L);
        step.setContactId(12L);
        when(steps.create(any())).thenReturn(step);
        when(steps.getById(900L)).thenReturn(step);
        when(steps.getByApplicationId(42L)).thenReturn(List.of(step));

        mvc.perform(post("/application-service/steps/application/42").param("userId", "7")
                        .contentType("application/json").content("""
                                {"stepStatus":"FIRST_CONTACT","stepDate":"2026-11-03T10:00:00","contactId":12}
                                """))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(900));
        mvc.perform(get("/application-service/steps/900").param("userId", "7"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.applicationId").value(42));
        mvc.perform(get("/application-service/steps").param("applicationId", "42").param("userId", "7"))
                .andExpect(jsonPath("$[0].contactId").value(12));
    }

    @Test
    void rejectsStepForAnotherUsersApplication() throws Exception {
        application.setUserId(99L);
        mvc.perform(get("/application-service/steps").param("applicationId", "42").param("userId", "7"))
                .andExpect(status().isNotFound());
        mvc.perform(get("/application-service/applications/42/detail").param("userId", "7"))
                .andExpect(status().isNotFound());
        verifyNoInteractions(steps, contacts, reminders);
    }

    @Test
    void rejectsStepWithoutStatusOrUserIdentity() throws Exception {
        mvc.perform(post("/application-service/steps/application/42").param("userId", "7")
                        .contentType("application/json").content("{}"))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/application-service/steps/900"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(steps);
    }

    private String creationRequest(boolean reminder) {
        return """
                {"companyName":"Acme","jobTitle":"Développeur Java","city":"Marseille",
                 "country":"France","source":"LinkedIn","workMode":"REMOTE",
                 "initialStatus":"APPLIED","userId":7,"createReminder":%s}
                """.formatted(reminder);
    }
}
