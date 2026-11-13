package com.jobscape.webgateway.controller;

import com.jobscape.webgateway.client.ApplicationStepClient;
import com.jobscape.webgateway.service.applicationDto.ApplicationStepDto;
import com.jobscape.webgateway.service.applicationDto.CreateApplicationStepRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/application")
@RequiredArgsConstructor
public class ApplicationStepController {

    private final ApplicationStepClient applicationStepClient;

    @PostMapping("/{applicationId}/steps")
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationStepDto createApplicationStep(
            @PathVariable Long applicationId,
            @RequestAttribute("authenticatedUserId") Long userId,
            @RequestBody CreateApplicationStepRequest request
    ) {
        if (applicationId <= 0 || request.getStepStatus() == null || request.getStepDate() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Valid applicationId, stepStatus and stepDate are required"
            );
        }
        return applicationStepClient.createApplicationStep(applicationId, userId, request);
    }

    @PutMapping("/steps/update/{stepId}")
    public ApplicationStepDto updateApplicationStep(
            @PathVariable Long stepId,
            @RequestAttribute("authenticatedUserId") Long userId,
            @RequestBody CreateApplicationStepRequest request
    ) {
        return  applicationStepClient.updateApplicationStep(stepId, userId, request);
    }

    @DeleteMapping("/steps/delete/{id}")
    public void  deleteApplicationStep(@PathVariable Long id) {
        applicationStepClient.deleteApplicationStepById(id);
    }


}
