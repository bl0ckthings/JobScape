package com.jobscape.webgateway.controller;

import com.jobscape.webgateway.client.ContactClient;
import com.jobscape.webgateway.service.contactDto.ContactSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactClient contactClient;

    @GetMapping("/user")
    public List<ContactSummaryDto> getContactsByUser(
            @RequestAttribute("authenticatedUserId") Long userId
    ) {
        return contactClient.getContactsByUser(userId);
    }

    @GetMapping("/company/{id}")
    public List<ContactSummaryDto> getContactsByCompany(@PathVariable Long id, @RequestAttribute("authenticatedUserId") Long userId) {
        return contactClient.getContactsByCompany(id, userId);
    }

    @GetMapping("{id}")
    public ContactSummaryDto getContactById(@PathVariable Long id, @RequestAttribute("authenticatedUserId") Long userId) {
        return contactClient.getContactById(id, userId);
    }

    @PostMapping("/create")
    public ContactSummaryDto createContact(@RequestBody ContactSummaryDto contact,
                                           @RequestAttribute("authenticatedUserid") Long userId) {
        return contactClient.createContact(contact, userId);
    }

    @PutMapping("/update/{id}")
    public ContactSummaryDto updateContact(@PathVariable Long id,
                                           @RequestAttribute("authenticatedUserid") Long userId,
                                           @RequestBody ContactSummaryDto contact
    ) {
        return contactClient.updateContact(id, userId, contact);
    }

    @DeleteMapping("/contacts/delete/{id}")
    public void deleteContactById(@PathVariable Long id, @RequestAttribute("authenticatedUserId") Long userId) {
        contactClient.deleteContactById(id, userId);
    }

}
