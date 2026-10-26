package com.jobscape.companyservice.client;

import com.jobscape.companyservice.dto.CompanyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("entity-manager")
public interface CompanyClient {

    @RequestMapping(method = RequestMethod.GET, value = "/internal/companies", consumes = "application/json")
    List<CompanyDto> getCompanies();

    @RequestMapping(method = RequestMethod.GET, value = "/internal/companies/{id}", consumes = "application/json")
    CompanyDto getCompanyById(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.POST, value= "/internal/companies/create", consumes = "application/json")
    CompanyDto createCompany(@RequestBody CompanyDto company);

    @RequestMapping(method = RequestMethod.POST, value= "/internal/companies/delete/{id}", consumes = "application/json")
    void deleteCompany(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/internal/companies/by-domain")
    CompanyDto getCompanyByDomain(@RequestParam("domain") String domain);
}
