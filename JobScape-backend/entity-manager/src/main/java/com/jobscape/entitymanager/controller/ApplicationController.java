package com.jobscape.entitymanager.controller;

import com.jobscape.entitymanager.model.Application;
import com.jobscape.entitymanager.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationRepository applicationRepository;

    @GetMapping("/{id}")
    public Application getApplicationById(@PathVariable Long id) {
        return applicationRepository.findById(id).orElse(null);
    }

    @GetMapping("/user/{id}")
    public List<Application> getApplicationsByUserId(@PathVariable Long id) {
        return applicationRepository.findByUserId(id);
    }

    @GetMapping
    public List<Application> findAll() {
        return applicationRepository.findAll();
    }


    @PostMapping("/create")
    public Application create(@RequestBody Application application) {
        return applicationRepository.save(application);
    }


    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        applicationRepository.deleteById(id);
    }

    @PutMapping("/update/{id}")
    public Application update(
            @PathVariable Long id,
            @RequestBody Application updatedApplication
    ) {
        Application existing = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (updatedApplication.getJobTitle() != null) {
            existing.setJobTitle(updatedApplication.getJobTitle());
        }

        if (updatedApplication.getCity() != null) {
            existing.setCity(updatedApplication.getCity());
        }

        if (updatedApplication.getCountry() != null) {
            existing.setCountry(updatedApplication.getCountry());
        }

        if (updatedApplication.getSource() != null) {
            existing.setSource(updatedApplication.getSource());
        }

        if (updatedApplication.getUrl() != null) {
            existing.setUrl(updatedApplication.getUrl());
        }

        if (updatedApplication.getStatus() != null) {
            existing.setStatus(updatedApplication.getStatus());
        }

        if (updatedApplication.getWorkMode() != null) {
            existing.setWorkMode(updatedApplication.getWorkMode());
        }

        return applicationRepository.save(existing);
    }
}
