package com.jobscape.companyservice.client;

import com.jobscape.companyservice.dto.ContactDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "entity-manager", contextId = "contactClient")
public interface ContactClient {

    @RequestMapping(method = RequestMethod.GET, value = "/internal/contact/company/{id}", consumes = "application/json")
    public List<ContactDto> getContactsByCompany(@PathVariable Long id, @RequestParam("userId") Long userId);

    @RequestMapping(method = RequestMethod.GET, value = "/internal/contact/{id}", consumes = "application/json")
    public ContactDto getContactById(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/internal/contact/user/{id}", consumes = "application/json")
    public List<ContactDto> getContactsByUserId(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.POST, value = "/internal/contact/create", consumes = "application/json")
    public ContactDto createContact(@RequestBody ContactDto contactDto);

    @RequestMapping(method = RequestMethod.PUT, value = "/internal/contact/update/{id}", consumes = "application/json")
    public ContactDto updateContact(@PathVariable Long id, @RequestBody ContactDto contactDto);

    @RequestMapping(method = RequestMethod.DELETE, value = "/internal/contact/delete/{id}", consumes = "application/json")
    public void deleteContactById(@PathVariable Long id);

}
