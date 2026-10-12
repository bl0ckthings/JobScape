package com.jobscape.webgateway.controller;

import com.jobscape.webgateway.client.CompanyClient;
import com.jobscape.webgateway.model.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyClient companyClient;

    @GetMapping()
    public List<Company> getCompanies() {
        return companyClient.getCompanies();
    }

    @GetMapping("/{id}")
    public Company getCompany(@PathVariable Long id) {
        return companyClient.getCompanyById(id);
    }


}
