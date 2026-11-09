package com.jobscape.reminderservice.service;

import com.jobscape.reminderservice.NotificationRepository;
import com.jobscape.reminderservice.model.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }
}
