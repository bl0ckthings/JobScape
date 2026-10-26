package com.jobscape.webgateway.service.applicationDto;

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
    private List<ApplicationStepDto> steps = new ArrayList<>();
}
