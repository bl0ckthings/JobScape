package com.jobscape.webgateway.controller;

import com.jobscape.webgateway.client.ApplicationClient;
import com.jobscape.webgateway.service.applicationDto.ApplicationDto;
import com.jobscape.webgateway.service.applicationDto.ApplicationsByStatusResponse;
import com.jobscape.webgateway.service.applicationDto.CreateApplicationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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


}
