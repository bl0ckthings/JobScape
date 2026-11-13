package com.jobscape.webgateway.client;

import com.jobscape.webgateway.service.applicationDto.ApplicationStepDto;
import com.jobscape.webgateway.service.applicationDto.CreateApplicationStepRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "application-service", contextId = "applicationStepClient")
public interface ApplicationStepClient {

    @PostMapping("/application-service/steps/application/{applicationId}")
    ApplicationStepDto createApplicationStep(
            @PathVariable Long applicationId,
            @RequestParam Long userId,
            @RequestBody CreateApplicationStepRequest request
    );

    @GetMapping("/application-service/steps/{id}")
    public ApplicationStepDto getApplicationStepById(@PathVariable Long id);

    @GetMapping("/application-service/steps")
    public List<ApplicationStepDto> getApplicationSteps(@RequestParam Long applicationId, @RequestParam Long userId);

    @PutMapping("/application-service/steps/update/{id}")
    public ApplicationStepDto updateApplicationStep(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestBody CreateApplicationStepRequest request
    );

    @DeleteMapping("/application-service/steps/delete/{id}")
    public void deleteApplicationStepById(@PathVariable Long id);





}
