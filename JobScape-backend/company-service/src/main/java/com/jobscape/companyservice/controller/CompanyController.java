package com.jobscape.companyservice.controller;

import com.jobscape.companyservice.client.CompanyClient;
import com.jobscape.companyservice.model.Company;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyClient companyClient;

    @GetMapping()
    public List<Company> getCompanies() {
        // TODO : HANDLE THE CASES WHERE THE LIST IS EMPTY
        return companyClient.getCompanies();
    }

    @GetMapping()
    public Company getCompanyById(@RequestParam Long id) {
        return companyClient.getCompanyById(id);
    }

    @PostMapping("/create")
    public Company createCompany(@RequestBody Company company) {
        return companyClient.createCompany(company);
    }

    @DeleteMapping("/delete")
    public void deleteCompany(@RequestParam Long id) {
        companyClient.deleteCompany(id);
    }
}
