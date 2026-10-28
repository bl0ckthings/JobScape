package com.jobscape.applicationservice.dto.application;

import com.jobscape.applicationservice.model.ApplicationCategory;
import com.jobscape.applicationservice.model.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Data

@RequiredArgsConstructor
public class ApplicationsByStatusResponse {

    private Map<ApplicationCategory, List<ApplicationDto>> applicationsByStatus;
}
