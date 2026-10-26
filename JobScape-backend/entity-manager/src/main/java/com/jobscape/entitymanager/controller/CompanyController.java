package com.jobscape.entitymanager.controller;

import com.jobscape.entitymanager.dto.CompanyDto;
import com.jobscape.entitymanager.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping()
    public List<CompanyDto> findAll() {
        return companyService.getAll();
    }

    @GetMapping("/{id}")
    public CompanyDto getCompanyById(@PathVariable Long id) {
        return companyService.getById(id);
    }

    @GetMapping("/by-domain")
    public ResponseEntity<CompanyDto> getCompanyByDomain(@RequestParam("domain") String domain) {
        return ResponseEntity.of(companyService.getCompanyByDomain(domain));
    }

    @PostMapping("/create")
    public CompanyDto create(@RequestBody CompanyDto company) {
        return companyService.create(company);
    }

    @PutMapping("/update/{id}")
    public CompanyDto update(@PathVariable Long id, @RequestBody CompanyDto company) {
        return companyService.update(id, company);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        companyService.delete(id);
    }
}
