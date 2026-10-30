package com.jobscape.reminderservice.client;

import com.jobscape.reminderservice.dto.ReminderDto;
import com.jobscape.reminderservice.dto.UpdateReminderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "entity-manager")
public interface ReminderClient {
    @RequestMapping(method = RequestMethod.GET, value = "/internal/reminders/{id}", consumes = "application/json")
    ReminderDto getReminderById(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/internal/reminders/applications/{applicationId}", consumes = "application/json")
    List<ReminderDto> getRemindersByApplicationId(@PathVariable long applicationId);


    @RequestMapping(method = RequestMethod.POST, value = "/internal/reminders/create", consumes = "application/json")
    public ReminderDto createReminder(@RequestBody ReminderDto reminderDto);

    @RequestMapping(method = RequestMethod.PUT, value = "/internal/reminders/update/{id}", consumes = "application/json")
    ReminderDto updateReminder(@PathVariable("id") Long id, @RequestParam("userId") Long userId,
                               @RequestBody UpdateReminderRequest updatedReminder);

    @RequestMapping(method = RequestMethod.DELETE, value = "/internal/reminders/delete", consumes = "application/json")
    public void deleteReminder(@RequestParam("id") Long id, @RequestParam("userId") Long userId);



}
