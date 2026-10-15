package com.jobscape.entitymanager.service;

import com.jobscape.entitymanager.dto.ApplicationDto;
import com.jobscape.entitymanager.dto.ApplicationStepDto;
import com.jobscape.entitymanager.dto.CompanyDto;
import com.jobscape.entitymanager.dto.CompanySummaryDto;
import com.jobscape.entitymanager.dto.ContactDto;
import com.jobscape.entitymanager.dto.ReminderDto;
import com.jobscape.entitymanager.exception.ResourceNotFoundException;
import com.jobscape.entitymanager.model.Application;
import com.jobscape.entitymanager.model.ApplicationStep;
import com.jobscape.entitymanager.model.Company;
import com.jobscape.entitymanager.model.Contact;
import com.jobscape.entitymanager.model.Reminder;
import com.jobscape.entitymanager.model.User;
import com.jobscape.entitymanager.repository.ApplicationRepository;
import com.jobscape.entitymanager.repository.ApplicationStepRepository;
import com.jobscape.entitymanager.repository.CompanyRepository;
import com.jobscape.entitymanager.repository.ContactRepository;
import com.jobscape.entitymanager.repository.ReminderRepository;
import com.jobscape.entitymanager.repository.UserRepository;
import com.jobscape.entitymanager.service.mapper.ApplicationMapper;
import com.jobscape.entitymanager.service.mapper.ApplicationStepMapper;
import com.jobscape.entitymanager.service.mapper.CompanyMapper;
import com.jobscape.entitymanager.service.mapper.ContactMapper;
import com.jobscape.entitymanager.service.mapper.ReminderMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CrudServiceTest {

    @Test
    void applicationServiceProvidesCrudWithDtos() {
        ApplicationRepository applications = mock(ApplicationRepository.class);
        CompanyRepository companies = mock(CompanyRepository.class);
        UserRepository users = mock(UserRepository.class);
        ApplicationMapper mapper = mock(ApplicationMapper.class);
        ApplicationService service = new ApplicationService(applications, companies, users, mapper);

        User user = new User();
        Company company = new Company();
        Application entity = new Application();
        ApplicationDto request = new ApplicationDto();
        request.setUserId(1L);
        request.setCompanyId(2L);
        ApplicationDto response = new ApplicationDto();

        when(users.findById(1L)).thenReturn(Optional.of(user));
        when(companies.findById(2L)).thenReturn(Optional.of(company));
        when(mapper.toEntity(request, user, company)).thenReturn(entity);
        when(applications.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(response);
        when(applications.findAll()).thenReturn(List.of(entity));
        when(applications.findById(3L)).thenReturn(Optional.of(entity));

        assertSame(response, service.create(request));
        assertEquals(List.of(response), service.getAll());
        assertSame(response, service.getById(3L));
        assertSame(response, service.update(3L, request));
        service.delete(3L);
        verify(applications).delete(entity);
    }

    @Test
    void companyServiceProvidesCrudWithDtos() {
        CompanyRepository companies = mock(CompanyRepository.class);
        CompanyMapper mapper = mock(CompanyMapper.class);
        CompanyService service = new CompanyService(companies, mapper);
        Company entity = new Company();
        CompanyDto request = new CompanyDto();
        request.setName("JobScape");
        request.setWebsite("https://jobscape.test");
        CompanyDto response = new CompanyDto();

        when(mapper.toEntity(request)).thenReturn(entity);
        when(companies.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(response);
        when(companies.findAll()).thenReturn(List.of(entity));
        when(companies.findById(1L)).thenReturn(Optional.of(entity));

        assertSame(response, service.create(request));
        assertEquals(List.of(response), service.getAll());
        assertSame(response, service.getById(1L));
        assertSame(response, service.update(1L, request));
        assertEquals("JobScape", entity.getName());
        service.delete(1L);
        verify(companies).delete(entity);
    }

    @Test
    void contactServiceResolvesRelationshipsAndProvidesCrud() {
        ContactRepository contacts = mock(ContactRepository.class);
        CompanyRepository companies = mock(CompanyRepository.class);
        UserRepository users = mock(UserRepository.class);
        ContactMapper mapper = mock(ContactMapper.class);
        ContactService service = new ContactService(contacts, companies, users, mapper);
        Company company = new Company();
        User user = new User();
        Contact entity = new Contact();
        ContactDto request = new ContactDto();
        CompanySummaryDto companyReference = new CompanySummaryDto();
        companyReference.setId(1L);
        request.setCompany(companyReference);
        request.setUserId(2L);
        ContactDto response = new ContactDto();

        when(companies.findById(1L)).thenReturn(Optional.of(company));
        when(users.findById(2L)).thenReturn(Optional.of(user));
        when(mapper.toEntity(request, company, user)).thenReturn(entity);
        when(contacts.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(response);
        when(contacts.findById(3L)).thenReturn(Optional.of(entity));
        when(contacts.findAll()).thenReturn(List.of(entity));

        assertSame(response, service.create(request));
        assertSame(response, service.getById(3L));
        assertEquals(List.of(response), service.getAll());
        assertSame(response, service.update(3L, request));
        service.delete(3L);
        verify(contacts).delete(entity);
    }

    @Test
    void applicationStepServiceResolvesRelationshipsAndProvidesCrud() {
        ApplicationStepRepository steps = mock(ApplicationStepRepository.class);
        ApplicationRepository applications = mock(ApplicationRepository.class);
        ContactRepository contacts = mock(ContactRepository.class);
        ApplicationStepMapper mapper = mock(ApplicationStepMapper.class);
        ApplicationStepService service = new ApplicationStepService(steps, applications, contacts, mapper);
        Application application = new Application();
        Contact contact = new Contact();
        ApplicationStep entity = new ApplicationStep();
        ApplicationStepDto request = new ApplicationStepDto();
        request.setApplicationId(1L);
        request.setContactId(2L);
        ApplicationStepDto response = new ApplicationStepDto();

        when(applications.findById(1L)).thenReturn(Optional.of(application));
        when(contacts.findById(2L)).thenReturn(Optional.of(contact));
        when(mapper.toEntity(request, application, contact)).thenReturn(entity);
        when(steps.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(response);
        when(steps.findById(3L)).thenReturn(Optional.of(entity));
        when(steps.findByApplicationId(1L)).thenReturn(List.of(entity));

        assertSame(response, service.create(request));
        assertSame(response, service.getById(3L));
        assertEquals(List.of(response), service.getByApplicationId(1L));
        assertSame(response, service.update(3L, request));
        service.delete(3L);
        verify(steps).delete(entity);
    }

    @Test
    void reminderServiceResolvesApplicationAndProvidesCrud() {
        ReminderRepository reminders = mock(ReminderRepository.class);
        ApplicationRepository applications = mock(ApplicationRepository.class);
        ReminderMapper mapper = mock(ReminderMapper.class);
        ReminderService service = new ReminderService(reminders, applications, mapper);
        Application application = new Application();
        Reminder entity = new Reminder();
        ReminderDto request = new ReminderDto();
        request.setApplicationId(1L);
        ReminderDto response = new ReminderDto();

        when(applications.findById(1L)).thenReturn(Optional.of(application));
        when(mapper.toEntity(request, application)).thenReturn(entity);
        when(reminders.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(response);
        when(reminders.findById(2L)).thenReturn(Optional.of(entity));
        when(reminders.findByApplicationId(1L)).thenReturn(List.of(entity));

        assertSame(response, service.create(request));
        assertSame(response, service.getById(2L));
        assertEquals(List.of(response), service.getByApplicationId(1L));
        assertSame(response, service.update(2L, request));
        service.delete(2L);
        verify(reminders).delete(entity);
    }

    @Test
    void userServiceProvidesCrudAndReportsMissingUsers() {
        UserRepository users = mock(UserRepository.class);
        UserService service = new UserService(users);
        User user = new User("Alice", "Martin", "alice@example.com", "hash");
        User update = new User("Alicia", null, null, null);

        when(users.save(user)).thenReturn(user);
        when(users.findById(1L)).thenReturn(Optional.of(user));
        when(users.findAll()).thenReturn(List.of(user));
        when(users.findByEmail("alice@example.com")).thenReturn(Optional.of(user));

        assertSame(user, service.create(user));
        assertSame(user, service.getById(1L));
        assertEquals(List.of(user), service.getAll(null));
        assertEquals(List.of(user), service.getAll("alice@example.com"));
        assertSame(user, service.update(1L, update));
        assertEquals("Alicia", user.getFirstName());
        service.delete(1L);
        verify(users).delete(user);

        when(users.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }
}
