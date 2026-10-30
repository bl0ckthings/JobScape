package com.jobscape.applicationservice.client;

import com.jobscape.applicationservice.dto.contact.ContactDto;
import com.jobscape.applicationservice.dto.contact.ContactSummaryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "company-service", contextId = "contactClient")
public interface ContactClient {

    @GetMapping("/contacts/company/{id}")
    List<ContactSummaryDto> getContactsByCompanyId(@PathVariable("id") Long companyId,
                                                  @RequestParam("userId") Long userId);

    @GetMapping("/contacts/user/{userId}")
    List<ContactSummaryDto> getContactsByUserId(@PathVariable("userId") Long userId);

    @GetMapping("/contacts/{id}")
    ContactSummaryDto getContactById(@PathVariable("id") Long contactId, @RequestParam("userId") Long userId);

    @PostMapping("/contacts/create")
    ContactSummaryDto createContact(@RequestBody ContactDto contact, @RequestParam("userId") Long userId);

    @PutMapping("/contacts/update/{id}")
    ContactSummaryDto updateContact(@PathVariable("id") Long contactId, @RequestParam("userId") Long userId,
                                   @RequestBody ContactDto contact);

    @DeleteMapping("/contacts/delete/{id}")
    void deleteContact(@PathVariable("id") Long contactId, @RequestParam("userId") Long userId);
}
