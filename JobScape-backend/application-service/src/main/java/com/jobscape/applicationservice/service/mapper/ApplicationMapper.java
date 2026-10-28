package com.jobscape.applicationservice.service.mapper;

import com.jobscape.applicationservice.dto.application.ApplicationDto;
import com.jobscape.applicationservice.dto.application.CreateApplicationRequest;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {

    public ApplicationDto toDto(CreateApplicationRequest request) {
        ApplicationDto application = new ApplicationDto();
        application.setJobTitle(request.getJobTitle());
        application.setCity(request.getCity());
        application.setCountry(request.getCountry());
        application.setSource(request.getSource());
        application.setUrl(request.getUrl());
        application.setStatus(request.getInitialStatus());
        application.setWorkMode(request.getWorkMode());
        application.setUserId(request.getUserId());
        return application;
    }
}
