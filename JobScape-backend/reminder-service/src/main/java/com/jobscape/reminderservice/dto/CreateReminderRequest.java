package com.jobscape.reminderservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CreateReminderRequest {

    @NotNull
    @Positive
    private Long applicationId;

    @NotNull
    private LocalDate applicationDate;

    @Size(max = 1000)
    private String comment;
}