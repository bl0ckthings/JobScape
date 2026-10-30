package com.jobscape.applicationservice.client;

import com.jobscape.applicationservice.dto.company.CompanyDto;
import com.jobscape.applicationservice.dto.company.CompanySelectionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "company-service", contextId = "companyClient")
public interface CompanyClient {

    @GetMapping("/companies")
    List<CompanyDto> getCompanies();

    @GetMapping("/companies/{id}")
    CompanyDto getCompanyById(@PathVariable("id") Long id);

    @GetMapping("/companies/domain")
    CompanyDto getCompanyByDomain(@RequestParam("domain") String domain);

    @PostMapping("/companies/create")
    CompanyDto createCompany(@RequestBody CompanyDto company);

    @PostMapping("/companies/resolve")
    CompanyDto resolveCompany(@RequestBody CompanySelectionDto selection);

    @DeleteMapping("/companies/delete")
    void deleteCompany(@RequestParam("id") Long id);
}
