package com.jobscape.webgateway.controller;

import com.jobscape.webgateway.client.ApplicationClient;
import com.jobscape.webgateway.service.applicationDto.ApplicationDetailDto;
import com.jobscape.webgateway.service.applicationDto.ApplicationDto;
import com.jobscape.webgateway.service.applicationDto.ApplicationsByStatusResponse;
import com.jobscape.webgateway.service.applicationDto.CreateApplicationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
@RequestMapping("/api/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationClient applicationClient;
    @PostMapping("/create")
    public ApplicationDto createApplication(
            @RequestBody CreateApplicationRequest request,
            @RequestAttribute("authenticatedUserId") Long userId
    ) {
        request.setUserId(userId);
        return applicationClient.createApplication(request);
    }
    @GetMapping("/user")
    public ApplicationsByStatusResponse  getApplicationsByUserId(@RequestAttribute("authenticatedUserId") Long userId) {
        return applicationClient.getApplicationsByUserId(userId);
    }

    @GetMapping("/{id}")
    public ApplicationDto getApplicationById(@PathVariable Long id, @RequestAttribute("authenticatedUserId") Long userId) {
        ApplicationDto application = applicationClient.getApplicationById(id);
        if (!Objects.equals(application.getUserId(), userId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Application not found"
            );
        }
        return application;


    }

    @GetMapping("/{id}/detail")
    public ApplicationDetailDto getApplicationDetailById(@PathVariable Long id, @RequestAttribute("authenticatedUserId") Long userId) {
        return applicationClient.getApplicationsDetail(id, userId);
    }

    @GetMapping("/detail/{id}")
    public ApplicationDetailDto getApplicationDetail(
            @PathVariable Long id,
            @RequestAttribute("authenticatedUserId") Long userId
    ) {
        return applicationClient.getApplicationsDetail(id, userId);
    }


}
