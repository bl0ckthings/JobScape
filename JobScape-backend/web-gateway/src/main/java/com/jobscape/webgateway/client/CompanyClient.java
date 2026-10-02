package com.jobscape.webgateway.client;

import com.jobscape.webgateway.model.Company;
import com.jobscape.webgateway.service.authDto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("company-service")
public interface CompanyClient {
    @RequestMapping(method = RequestMethod.GET, value = "/companies", consumes = "application/json")
    List<Company> getCompanies();

    @RequestMapping(method = RequestMethod.GET, value = "/companies", consumes = "application/json")
    Company getCompanyById(@RequestParam Long id);

    @RequestMapping(method = RequestMethod.POST, value = "/companies/create", consumes = "application/json")
    Company createCompany(@RequestBody Company company);

    @RequestMapping(method = RequestMethod.DELETE, value = "/companies/delete", consumes = "application/json")
    void createCompany(@RequestParam Long id);
}
