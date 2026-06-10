package com.jobscape.applicationservice.client;

import com.jobscape.applicationservice.model.Application;
import com.jobscape.applicationservice.model.ApplicationStep;
import com.jobscape.applicationservice.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("entity-manager")
public interface ApplicationClient {
    @RequestMapping(method = RequestMethod.GET, value = "/applications/{id}", consumes = "application/json")
    Application getApplicationById(@PathVariable long id);

    @RequestMapping(method = RequestMethod.GET, value = "/applications/user/{id}", consumes = "application/json")
    List<Application> getApplicationByUserId(@PathVariable long id);

    @RequestMapping(method = RequestMethod.GET, value = "/applications", consumes = "application/json")
    List<Application> getAllApplications();

    @RequestMapping(method = RequestMethod.POST, value = "/applications/create", consumes = "application/json")
    Application create(@RequestBody Application application);

    @RequestMapping(method = RequestMethod.DELETE, value = "/applications/delete/{id}", consumes = "application/json")
    void delete(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.PUT, value = "/applications/update/{id}", consumes = "application/json")
    Application update(@PathVariable Long id, @RequestBody Application application);

}
