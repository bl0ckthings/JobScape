package com.jobscape.webgateway.client;

import com.jobscape.webgateway.service.applicationDto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "application-service")
public interface ApplicationClient {
    @RequestMapping(method = RequestMethod.POST, value = "/application-service/applications/create", consumes = "application/json")
    public ApplicationDto createApplication(@RequestBody CreateApplicationRequest createApplicationRequest);

    @RequestMapping(method = RequestMethod.GET, value = "/application-service/applications/user/{id}", consumes = "application/json")
    public ApplicationsByStatusResponse getApplicationsByUserId(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/application-service/applications/{id}", consumes = "application/json")
    public ApplicationDto getApplicationById(@PathVariable Long id);

    @GetMapping("/application-service/applications/{id}/detail")
    public ApplicationDetailDto getApplicationsDetail(@PathVariable("id") Long id, @RequestParam("userId") Long userId);

}
