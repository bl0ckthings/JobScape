package com.jobscape.webgateway.service.applicationDto;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.jobscape.webgateway.service.companyDto.CompanyDto;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ApplicationsByStatusResponse {
    private Map<ApplicationCategory, List<ApplicationDto>> applicationsByStatus;
    private Map<Long, CompanyDto> companiesById = Map.of();
}
