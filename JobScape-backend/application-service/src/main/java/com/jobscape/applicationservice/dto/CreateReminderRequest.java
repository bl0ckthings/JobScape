package com.jobscape.applicationservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CreateReminderRequest {
    private LocalDate applicationDate;
    private String comment;
    private Long applicationId;
}
