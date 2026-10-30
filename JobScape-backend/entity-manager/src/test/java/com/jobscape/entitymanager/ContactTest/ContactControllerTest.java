package com.jobscape.entitymanager.ContactTest;

import com.jobscape.entitymanager.controller.ContactController;
import com.jobscape.entitymanager.dto.CompanySummaryDto;
import com.jobscape.entitymanager.dto.ContactDto;
import com.jobscape.entitymanager.model.Company;
import com.jobscape.entitymanager.model.Contact;
import com.jobscape.entitymanager.model.User;
import com.jobscape.entitymanager.repository.CompanyRepository;
import com.jobscape.entitymanager.repository.ContactRepository;
import com.jobscape.entitymanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ContactControllerTest {

    @Autowired ContactController controller;
    @Autowired ContactRepository contactRepository;
    @Autowired CompanyRepository companyRepository;
    @Autowired UserRepository userRepository;

    @Test
    void createContactTest() {
        Company company = companyRepository.save(new Company("https://jobscape.test", "JobScape"));
        User user = userRepository.save(new User("Alice", "Martin", "contact-create@test.com", "hash"));
        CompanySummaryDto companyDto = new CompanySummaryDto();
        companyDto.setId(company.getId());
        ContactDto contact = new ContactDto();
        contact.setName("Recruiter");
        contact.setCompany(companyDto);
        contact.setUserId(user.getId());

        ContactDto saved = controller.createContact(contact);

        assertNotNull(saved.getId());
        assertEquals(company.getId(), saved.getCompany().getId());
    }

    @Test
    void getContactsByCompanyTest() {
        Company company = companyRepository.save(new Company("https://jobscape.test", "JobScape"));
        User user = userRepository.save(new User("Alice", "Martin", "contact-read@test.com", "hash"));
        User otherUser = userRepository.save(new User("Bob", "Martin", "contact-other@test.com", "hash"));
        Contact contact = new Contact();
        contact.setName("Recruiter");
        contact.setCompany(company);
        contact.setUser(user);
        contactRepository.save(contact);

        Contact otherContact = new Contact();
        otherContact.setName("Other recruiter");
        otherContact.setCompany(company);
        otherContact.setUser(otherUser);
        contactRepository.save(otherContact);

        List<ContactDto> result = controller.getContactsByCompany(company.getId(), user.getId());

        assertEquals(1, result.size());
        assertEquals(contact.getId(), result.get(0).getId());
        assertEquals(user.getId(), result.get(0).getUserId());
    }
}
