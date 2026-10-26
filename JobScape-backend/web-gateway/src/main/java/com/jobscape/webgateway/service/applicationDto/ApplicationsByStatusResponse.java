package com.jobscape.webgateway.service.applicationDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ApplicationsByStatusResponse {
    private Map<ApplicationCategory, List<ApplicationDto>> applicationsByStatus;
}
