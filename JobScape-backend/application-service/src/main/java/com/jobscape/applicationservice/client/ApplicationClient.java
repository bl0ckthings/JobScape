package com.jobscape.applicationservice.client;

import com.jobscape.applicationservice.dto.ApplicationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "entity-manager", contextId = "applicationClient")
public interface ApplicationClient {
    @RequestMapping(method = RequestMethod.GET, value = "/internal/applications/{id}", consumes = "application/json")
    ApplicationDto getApplicationById(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/internal/applications/user/{id}", consumes = "application/json")
    List<ApplicationDto> getApplicationByUserId(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/internal/applications", consumes = "application/json")
    List<ApplicationDto> getAllApplications();

    @RequestMapping(method = RequestMethod.POST, value = "/internal/applications/create", consumes = "application/json")
    ApplicationDto create(@RequestBody ApplicationDto application);

    @RequestMapping(method = RequestMethod.DELETE, value = "/internal/applications/delete/{id}", consumes = "application/json")
    void delete(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.PUT, value = "/internal/applications/update/{id}", consumes = "application/json")
    ApplicationDto update(@PathVariable Long id, @RequestBody ApplicationDto application);

}
