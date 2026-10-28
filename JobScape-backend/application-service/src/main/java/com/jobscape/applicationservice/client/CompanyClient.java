package com.jobscape.applicationservice.client;

import com.jobscape.applicationservice.dto.company.CompanyDto;
import com.jobscape.applicationservice.dto.company.CompanySelectionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "company-service")
public interface CompanyClient {
    @PostMapping("/companies/resolve")
    CompanyDto resolveCompany(@RequestBody CompanySelectionDto selection);
}
