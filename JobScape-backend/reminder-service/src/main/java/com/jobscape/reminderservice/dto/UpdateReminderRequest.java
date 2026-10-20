package com.jobscape.reminderservice.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UpdateReminderRequest {

    private LocalDateTime reminderDate;

    @Size(max = 1000)
    private String comment;
}
