package com.jobscape.webgateway.service.companyDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CompanyDto {
    private Long id;
    private String name;
    private String domain;
    private String website;
    private String logoUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
