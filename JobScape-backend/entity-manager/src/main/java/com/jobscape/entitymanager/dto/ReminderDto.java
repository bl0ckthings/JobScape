package com.jobscape.entitymanager.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class ReminderDto {

    private Long id;
    private LocalDateTime reminderDate;
    private String comment;
    private LocalDateTime createdAt;
    private Long applicationId;
}
