package com.jobscape.applicationservice.dto;

import com.jobscape.applicationservice.model.ApplicationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ApplicationStepDto {
    private Long id;
    private String comment;
    private LocalDateTime stepDate;
    private ApplicationStatus stepStatus;
    private Long applicationId;
    private Long contactId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
