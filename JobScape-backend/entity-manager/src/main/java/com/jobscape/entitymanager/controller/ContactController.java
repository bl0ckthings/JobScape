package com.jobscape.entitymanager.controller;

import com.jobscape.entitymanager.dto.ContactDto;
import com.jobscape.entitymanager.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/contact")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;

    @GetMapping("/{id}")
    public ContactDto getContactById(@PathVariable Long id) {
        return contactService.getById(id);
    }

    @GetMapping("/company/{companyId}")
    public List<ContactDto> getContactsByCompany(@PathVariable Long companyId) {
        return contactService.getByCompanyId(companyId);
    }

    @GetMapping
    public List<ContactDto> getAllContacts() {
        return contactService.getAll();
    }

    @PostMapping("/create")
    public ContactDto createContact(@RequestBody ContactDto contact) {
        return contactService.create(contact);
    }

    @PutMapping("/update/{id}")
    public ContactDto updateContact(@PathVariable Long id, @RequestBody ContactDto contact) {
        return contactService.update(id, contact);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestBody ContactDto contact) {
        contactService.delete(contact.getId());
    }
}
