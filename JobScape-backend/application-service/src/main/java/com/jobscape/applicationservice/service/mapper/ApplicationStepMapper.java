package com.jobscape.applicationservice.service.mapper;

import com.jobscape.applicationservice.dto.application.ApplicationDto;
import com.jobscape.applicationservice.dto.application.CreateApplicationRequest;
import com.jobscape.applicationservice.dto.applicationstep.ApplicationStepDto;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStepMapper {

    public ApplicationStepDto toInitialDto(CreateApplicationRequest request, ApplicationDto application) {
        ApplicationStepDto step = new ApplicationStepDto();
        step.setApplicationId(application.getId());
        step.setStepStatus(application.getStatus());
        step.setComment(request.getComment());
        if (request.getInitialDate() != null) {
            step.setStepDate(request.getInitialDate().atStartOfDay());
        }
        return step;
    }
}
