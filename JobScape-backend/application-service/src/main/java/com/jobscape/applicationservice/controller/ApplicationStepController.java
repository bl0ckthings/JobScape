package com.jobscape.applicationservice.controller;

import com.jobscape.applicationservice.dto.applicationstep.ApplicationStepDto;
import com.jobscape.applicationservice.dto.applicationstep.CreateApplicationStepRequest;
import com.jobscape.applicationservice.service.ApplicationStepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/application-service/steps")
@RequiredArgsConstructor
public class ApplicationStepController {

    private final ApplicationStepService applicationStepService;

    @GetMapping("/{id}")
    public ApplicationStepDto getApplicationStepById(@PathVariable Long id, @RequestParam Long userId) {
        return applicationStepService.getStepById(id, userId);
    }

    @GetMapping()
    public List<ApplicationStepDto> getByApplicationId(@RequestParam Long applicationId, @RequestParam Long userId) {
        return applicationStepService.getByApplicationId(applicationId, userId);
    }

    @PostMapping("/application/{applicationId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationStepDto create(
            @PathVariable Long applicationId,
            @RequestParam Long userId,
            @RequestBody CreateApplicationStepRequest request
    ) {
        return applicationStepService.createStep(applicationId, userId, request);
    }

    @PutMapping("/update/{id}")
    public ApplicationStepDto update(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestBody CreateApplicationStepRequest request
    ) {
        return applicationStepService.updateStep(id, userId, request);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
         applicationStepService.deleteStep(id);
    }
}
