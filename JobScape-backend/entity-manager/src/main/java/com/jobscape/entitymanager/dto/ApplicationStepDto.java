package com.jobscape.entitymanager.dto;

import com.jobscape.entitymanager.model.ApplicationStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ApplicationStepDto {
    private Long id;
    private String comment;
    private LocalDateTime stepDate;
    private ApplicationStatus stepStatus;
    private Long contactId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
