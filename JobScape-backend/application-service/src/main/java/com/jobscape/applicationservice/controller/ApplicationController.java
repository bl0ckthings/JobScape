package com.jobscape.applicationservice.controller;

import com.jobscape.applicationservice.dto.application.ApplicationDetailDto;
import com.jobscape.applicationservice.dto.application.ApplicationDto;
import com.jobscape.applicationservice.dto.application.ApplicationsByStatusResponse;
import com.jobscape.applicationservice.dto.application.CreateApplicationRequest;
import com.jobscape.applicationservice.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/application-service/applications")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;



    @GetMapping("/{id}")
    public ApplicationDto getApplicationById(@PathVariable Long id) {

        return applicationService.getApplicationById(id);
    }

    @GetMapping("/user/{id}")
    public ApplicationsByStatusResponse getApplicationByUserId(@PathVariable Long id) {
        return applicationService.getApplicationsByStatus(id);
    }

    @GetMapping("/{id}/detail")
    public ApplicationDetailDto getApplicationDetailById(@PathVariable Long id, @RequestParam Long userId) {
        return applicationService.getApplicationDetail(id, userId);
    }


    @PostMapping("/create")
    public ApplicationDto createApplication(@Valid @RequestBody CreateApplicationRequest createApplicationRequest) {
       return applicationService.create(createApplicationRequest);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteApplicationById(@PathVariable Long id, @RequestParam Long userId) {
        applicationService.deleteApplicationById(id, userId);
    }

    @PutMapping("/update")
    public ApplicationDto updateApplication(@RequestBody ApplicationDto updatedApplication, @RequestParam Long userId) {
        return applicationService.updateApplication(updatedApplication, userId);
    }



}
