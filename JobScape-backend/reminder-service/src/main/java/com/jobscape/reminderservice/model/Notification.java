package com.jobscape.reminderservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;

    private Long userId;
    private Long reminderId;
    private Long applicationId;

    private NotificationType type;
    private String message;

    private Instant dueAt;
    private Instant createdAt;
    private Instant readAt;

}
