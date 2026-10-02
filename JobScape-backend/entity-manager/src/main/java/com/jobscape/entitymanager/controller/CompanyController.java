package com.jobscape.entitymanager.controller;

import com.jobscape.entitymanager.model.Company;
import com.jobscape.entitymanager.repository.ApplicationRepository;
import com.jobscape.entitymanager.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/companies")
public class CompanyController {

    @Autowired
    CompanyRepository companyRepository;

    @GetMapping()
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @GetMapping("/{id}")
    public Company getCompanyById(@PathVariable Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    }

    @PostMapping("/create")
    public Company create(@RequestBody Company company) {
        return companyRepository.save(company);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        Company company = companyRepository.findById(id).get();
        companyRepository.delete(company);
    }

}
