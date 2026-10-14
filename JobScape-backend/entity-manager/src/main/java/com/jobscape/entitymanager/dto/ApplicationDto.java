package com.jobscape.entitymanager.dto;

import com.jobscape.entitymanager.model.ApplicationStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApplicationDto {
    private Long id;
    private String jobTitle;
    private String city;
    private String country;
    private String source;
    private String url;
    private ApplicationStatus status;
    private String workMode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long userId;
    private Long companyId;
    private List<ApplicationStepDto> steps;
}