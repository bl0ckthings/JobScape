package com.jobscape.applicationservice.dto.company;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CompanyDto {
    private Long id;
    private String name;
    private String domain;
    private String website;
    private String logoUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
