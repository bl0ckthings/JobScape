package com.jobscape.webgateway.client;

import com.jobscape.webgateway.service.applicationDto.ApplicationDto;
import com.jobscape.webgateway.service.applicationDto.ApplicationsByStatusResponse;
import com.jobscape.webgateway.service.applicationDto.CreateApplicationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "application-service")
public interface ApplicationClient {
    @RequestMapping(method = RequestMethod.POST, value = "/application-service/applications/create", consumes = "application/json")
    public ApplicationDto createApplication(@RequestBody CreateApplicationRequest createApplicationRequest);

    @RequestMapping(method = RequestMethod.GET, value = "/application-service/applications/user/{id}", consumes = "application/json")
    public ApplicationsByStatusResponse getApplicationsByUserId(@PathVariable Long id);

}
