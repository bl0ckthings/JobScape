package com.jobscape.applicationservice.client;

import com.jobscape.applicationservice.dto.ApplicationStepDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "entity-manager", contextId = "applicationStepClient")
public interface ApplicationStepClient {
    @RequestMapping(method = RequestMethod.GET, value = "/internal/steps/{id}", consumes = "application/json")
    public ApplicationStepDto getById(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/internal/steps/application/{applicationId}", consumes = "application/json")
    public List<ApplicationStepDto> getByApplicationId(@PathVariable Long applicationId);

    @RequestMapping(method = RequestMethod.POST, value = "/internal/steps/create", consumes = "application/json")
    public ApplicationStepDto create(@RequestBody ApplicationStepDto appStepDto);

    @RequestMapping(method = RequestMethod.PUT, value = "/internal/steps/update/{id}", consumes = "application/json")
    public ApplicationStepDto update(@PathVariable Long id, @RequestBody ApplicationStepDto updatedAppStepDto);

    @RequestMapping(method = RequestMethod.DELETE, value = "/internal/steps/delete/{id}", consumes = "application/json")
    public void delete(@PathVariable Long id);
}
