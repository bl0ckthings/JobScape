package com.jobscape.reminderservice.controller;

import com.jobscape.reminderservice.model.Notification;
import com.jobscape.reminderservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/create")
    public Notification createNotification(@RequestBody Notification notification) {
        return notificationService.createNotification(notification);
    }
}
