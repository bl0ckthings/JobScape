package com.jobscape.entitymanager.CompanyTest;

import com.jobscape.entitymanager.controller.CompanyController;
import com.jobscape.entitymanager.dto.CompanyDto;
import com.jobscape.entitymanager.model.Company;
import com.jobscape.entitymanager.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CompanyControllerTest {

    @Autowired
    CompanyController controller;

    @Autowired
    CompanyRepository companyRepository;

    @Test
    void createCompanyTest() {
        CompanyDto company = new CompanyDto();
        company.setName("JobScape");
        company.setWebsite("https://jobscape.test");

        CompanyDto saved = controller.create(company);

        assertNotNull(saved.getId());
        assertEquals(company.getName(), saved.getName());
    }

    @Test
    void getCompanyByIdTest() {
        Company company = companyRepository.save(new Company("https://jobscape.test", "JobScape"));

        CompanyDto result = controller.getCompanyById(company.getId());

        assertEquals(company.getId(), result.getId());
    }

    @Test
    void getCompanyByDomainReturnsCompanyOrNotFound() {
        Company company = new Company("https://jobscape.test", "JobScape");
        company.setDomain("jobscape.test");
        companyRepository.save(company);

        assertEquals(company.getId(), controller.getCompanyByDomain("jobscape.test").getBody().getId());
        assertEquals(HttpStatus.NOT_FOUND, controller.getCompanyByDomain("missing.test").getStatusCode());
    }
}
