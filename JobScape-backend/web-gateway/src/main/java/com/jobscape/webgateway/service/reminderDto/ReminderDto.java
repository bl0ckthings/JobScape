package com.jobscape.webgateway.service.reminderDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ReminderDto {
    private Long id;
    private LocalDateTime reminderDate;
    private String comment;
    private Long applicationId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
