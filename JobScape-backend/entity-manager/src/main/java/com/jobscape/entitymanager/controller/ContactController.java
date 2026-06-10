package com.jobscape.entitymanager.controller;

import com.jobscape.entitymanager.model.Contact;
import com.jobscape.entitymanager.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contact")
public class ContactController {
    @Autowired
    ContactRepository contactRepository;

    @GetMapping("/{id}")
    public Contact getContactById(@PathVariable Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    @GetMapping("/company/{id}")
    public List<Contact> getContactSteps(@PathVariable Long id) {
        return contactRepository.findByCompanyId(id);
    }

    @GetMapping
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    @PostMapping("/create")
    public Contact createContact(@RequestBody Contact contact) {
        return contactRepository.save(contact);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestBody Contact contact) {
        contactRepository.delete(contact);
    }


}
