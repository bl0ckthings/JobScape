package com.jobscape.companyservice.client;

import com.jobscape.companyservice.model.Company;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("entity-manager")
public interface CompanyClient {

    @RequestMapping(method = RequestMethod.GET, value = "/internal/companies", consumes = "application/json")
    List<Company> getCompanies();

    @RequestMapping(method = RequestMethod.GET, value = "/internal/companies/{id}", consumes = "application/json")
    Company getCompanyById(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.POST, value= "/internal/companies/create", consumes = "application/json")
    Company createCompany(@RequestBody Company company);

    @RequestMapping(method = RequestMethod.POST, value= "/internal/companies/delete/{id}", consumes = "application/json")
    Company deleteCompany(@PathVariable Long id);
}
