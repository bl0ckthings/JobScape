package com.jobscape.companyservice.controller;

import com.jobscape.companyservice.dto.CompanyDto;
import com.jobscape.companyservice.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping()
    public List<CompanyDto> getCompanies() {
        // TODO : HANDLE THE CASES WHERE THE LIST IS EMPTY
        return companyService.getCompanies();
    }

    @GetMapping("/{id}")
    public CompanyDto getCompanyById(@PathVariable Long id) {
        return companyService.getCompanyById(id);
    }

    @PostMapping("/create")
    public CompanyDto createCompany(@RequestBody CompanyDto company) {
        return companyService.createCompany(company);
    }

    @DeleteMapping("/delete")
    public void deleteCompany(@RequestParam Long id) {
        companyService.deleteCompany(id);
    }

    @GetMapping("/domain")
    public ResponseEntity<CompanyDto> getCompanyByDomain(@RequestParam("domain") String domain) {
        Optional<CompanyDto> company = companyService.findCompanyByDomain(domain);
        return ResponseEntity.of(company);
    }
}
