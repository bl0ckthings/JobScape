package com.jobscape.webgateway.client;

import com.jobscape.webgateway.service.contactDto.ContactSummaryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("company-service")
public interface ContactClient {

    @GetMapping("/contacts/company/{id}")
    public List<ContactSummaryDto> getContactsByCompany(
            @PathVariable Long id,
            @RequestParam Long userId);

    @GetMapping("/contacts/user/{userId}")
    public List<ContactSummaryDto> getContactsByUser(@PathVariable Long userId);

    @GetMapping("/contacts/{id}")
    public ContactSummaryDto getContactById(@PathVariable Long id, @RequestParam Long userId);

    @PostMapping("/contacts/create")
    public ContactSummaryDto createContact(@RequestBody ContactSummaryDto contact, @RequestParam Long userId);

    @PutMapping("/contacts/update/{id}")
    public ContactSummaryDto updateContact(@PathVariable Long id,
                                           @RequestParam Long userId,
                                           @RequestBody ContactSummaryDto contact);

    @DeleteMapping("/contacts/delete/{id}")
    public void deleteContactById(@PathVariable Long id, @RequestParam Long userId);


}
