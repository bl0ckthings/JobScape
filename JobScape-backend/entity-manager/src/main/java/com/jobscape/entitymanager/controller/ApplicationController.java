package com.jobscape.entitymanager.controller;

import com.jobscape.entitymanager.dto.ApplicationDto;
import com.jobscape.entitymanager.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping("/{id}")
    public ApplicationDto getApplicationById(@PathVariable Long id) {

        return applicationService.getById(id);

    }

    @GetMapping("/user/{userId}")
    public List<ApplicationDto> getApplicationsByUserId(@PathVariable Long userId) {
        return applicationService.getByUserId(userId);
    }

    @GetMapping
    public List<ApplicationDto> findAll() {
        return applicationService.getAll();
    }

    @PostMapping("/create")
    public ApplicationDto create(@RequestBody ApplicationDto application) {

        return applicationService.create(application);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {

        applicationService.delete(id);
    }

    @PutMapping("/update/{id}")
    public ApplicationDto update(
            @PathVariable Long id,
            @RequestBody ApplicationDto updatedApplication
    ) {
        return applicationService.update(id, updatedApplication);
    }
}
