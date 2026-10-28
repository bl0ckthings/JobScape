package com.jobscape.applicationservice.client;

import com.jobscape.applicationservice.dto.reminder.CreateReminderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "reminder-service")
public interface ReminderClient {

    @RequestMapping(method = RequestMethod.POST, value = "/reminder/create", consumes = "application/json")
    void create(@RequestBody CreateReminderRequest request);


}
