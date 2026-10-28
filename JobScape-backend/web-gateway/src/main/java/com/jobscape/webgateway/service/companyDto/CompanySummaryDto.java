package com.jobscape.webgateway.service.companyDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompanySummaryDto {
    private Long id;
    private String name;
    private String website;
}
