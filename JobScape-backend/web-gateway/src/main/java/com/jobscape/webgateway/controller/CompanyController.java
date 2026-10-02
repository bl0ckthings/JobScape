package com.jobscape.webgateway.controller;

import com.jobscape.webgateway.client.CompanyClient;
import com.jobscape.webgateway.model.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyClient companyClient;

    @GetMapping()
    public List<Company> getCompanies() {
        return companyClient.getCompanies();
    }

    @GetMapping()
    public Company getCompany(@RequestParam Long id) {
        return companyClient.getCompanyById(id);
    }


}
