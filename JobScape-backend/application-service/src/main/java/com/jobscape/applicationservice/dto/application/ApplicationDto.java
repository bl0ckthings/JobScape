package com.jobscape.applicationservice.dto.application;

import com.jobscape.applicationservice.dto.applicationstep.ApplicationStepDto;
import com.jobscape.applicationservice.model.ApplicationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
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
    private String companyName;
    private String companyDomain;
    private String companyLogoUrl;
    private List<ApplicationStepDto> steps = new ArrayList<>();
}
