package com.jobscape.entitymanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReminderDto {
    private Long id;
    private LocalDateTime reminderDate;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long applicationId;
}
