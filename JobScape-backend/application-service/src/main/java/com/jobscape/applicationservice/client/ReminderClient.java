package com.jobscape.applicationservice.client;

import com.jobscape.applicationservice.dto.reminder.CreateReminderRequest;
import com.jobscape.applicationservice.dto.reminder.ReminderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "reminder-service")
public interface ReminderClient {

    @RequestMapping(method = RequestMethod.POST, value = "/reminder/create", consumes = "application/json")
    ReminderDto create(@RequestBody CreateReminderRequest request);

    @RequestMapping(method = RequestMethod.GET, value = "/reminder/application/{id}", consumes = "application/json")
    List<ReminderDto> getRemindersByApplicationId(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/reminder/{id}", consumes = "application/json")
    ReminderDto getReminderById(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.PUT, value = "/reminder/update/{id}", consumes = "application/json")
    ReminderDto update(@PathVariable Long id, @RequestBody ReminderDto request);

    @RequestMapping(method = RequestMethod.DELETE, value = "/reminder/delete/{id}", consumes = "application/json")
    void delete(@PathVariable Long id);
}
