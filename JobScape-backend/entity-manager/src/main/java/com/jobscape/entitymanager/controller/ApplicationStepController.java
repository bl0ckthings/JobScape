package com.jobscape.entitymanager.controller;

import com.jobscape.entitymanager.dto.ApplicationStepDto;
import com.jobscape.entitymanager.service.ApplicationStepService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/steps")
@RequiredArgsConstructor
public class ApplicationStepController {
    private final ApplicationStepService applicationStepService;

    @GetMapping("/{id}")
    public ApplicationStepDto getById(@PathVariable Long id) {
        return applicationStepService.getById(id);
    }

    @GetMapping
    public List<ApplicationStepDto> getAll() {
        return applicationStepService.getAll();
    }

    @GetMapping("/application/{applicationId}")
    public List<ApplicationStepDto> getStepsByApplicationId(@PathVariable Long applicationId) {
        return applicationStepService.getByApplicationId(applicationId);
    }

    @PostMapping("/create")
    public ApplicationStepDto createStep(@RequestBody ApplicationStepDto step) {
        return applicationStepService.create(step);
    }

    @DeleteMapping("/delete")
    public void deleteStep(@RequestBody Long id) {
        applicationStepService.delete(id);
    }

    @PutMapping("/update/{id}")
    public ApplicationStepDto updateStep(
            @PathVariable Long id,
            @RequestBody ApplicationStepDto updatedStep
    ) {
        return applicationStepService.update(id, updatedStep);
    }
}
