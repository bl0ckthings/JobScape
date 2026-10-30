package com.jobscape.companyservice.controller;

import com.jobscape.companyservice.dto.ContactDto;
import com.jobscape.companyservice.dto.ContactSummaryDto;
import com.jobscape.companyservice.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;

    // Routes internes : le gateway transmet l'identité issue du JWT.
    @GetMapping("/company/{id}")
    public List<ContactSummaryDto> getContactsByCompany(@PathVariable Long id, @RequestParam Long userId) {
        return contactService.getContactsByCompany(id, userId);
    }

    @GetMapping("/user/{userId}")
    public List<ContactSummaryDto> getContactsByUserId(@PathVariable Long userId) {
        return contactService.getContactsByUserId(userId);
    }

    @GetMapping("/{id}")
    public ContactSummaryDto getContactById(@PathVariable Long id, @RequestParam Long userId) {
        return contactService.getContactById(id, userId);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ContactSummaryDto createContact(@RequestBody ContactDto contact, @RequestParam Long userId) {
        return contactService.createContact(contact, userId);
    }

    @PutMapping("/update/{id}")
    public ContactSummaryDto updateContact(@PathVariable Long id, @RequestParam Long userId,
                                    @RequestBody ContactDto contact) {
        return contactService.updateContact(id, userId, contact);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContact(@PathVariable Long id, @RequestParam Long userId) {
        contactService.deleteContact(id, userId);
    }
}
