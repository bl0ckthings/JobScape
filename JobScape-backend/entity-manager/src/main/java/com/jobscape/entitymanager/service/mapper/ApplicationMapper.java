package com.jobscape.entitymanager.service.mapper;

import com.jobscape.entitymanager.dto.ApplicationDto;
import com.jobscape.entitymanager.model.Application;
import com.jobscape.entitymanager.model.Company;
import com.jobscape.entitymanager.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationMapper {
    private final ApplicationStepMapper applicationStepMapper;

    // Entity -> DTO: outgoing response
    public ApplicationDto toDto(Application application) {
        ApplicationDto dto = new ApplicationDto();

        dto.setId(application.getId());
        dto.setJobTitle(application.getJobTitle());
        dto.setCity(application.getCity());
        dto.setCountry(application.getCountry());
        dto.setSource(application.getSource());
        dto.setUrl(application.getUrl());
        dto.setStatus(application.getStatus());
        dto.setWorkMode(application.getWorkMode());
        dto.setCreatedAt(application.getCreatedAt());
        dto.setUpdatedAt(application.getUpdatedAt());
        dto.setUserId(application.getUser().getId());
        dto.setCompanyId(application.getCompany().getId());
        dto.setSteps(application.getSteps().stream()
                .map(applicationStepMapper::toDto)
                .toList());

        return dto;
    }

    // DTO -> Entity: incoming request
    public Application toEntity(
            ApplicationDto dto,
            User user,
            Company company
    ) {
        Application application = new Application();

        application.setJobTitle(dto.getJobTitle());
        application.setCity(dto.getCity());
        application.setCountry(dto.getCountry());
        application.setSource(dto.getSource());
        application.setUrl(dto.getUrl());
        application.setStatus(dto.getStatus());
        application.setWorkMode(dto.getWorkMode());
        application.setUser(user);
        application.setCompany(company);

        return application;
    }
}
