package com.jobscape.entitymanager.dto;

import com.jobscape.entitymanager.model.ApplicationStatus;
import com.jobscape.entitymanager.model.Company;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class ApplicationDto {
    private Long id;
    private String jobTitle;
    private String country;
    private String source;
    private String url;
    private ApplicationStatus status;
    private String workMode;
    private LocalDateTime createdAt;

    private Long userId;
    private Long companyId;
    private List<ApplicationStepDto> steps;
}
