package com.jobscape.entitymanager.service;

import com.jobscape.entitymanager.dto.ApplicationDto;
import com.jobscape.entitymanager.model.Application;
import com.jobscape.entitymanager.model.ApplicationStep;
import com.jobscape.entitymanager.model.Company;
import com.jobscape.entitymanager.model.User;
import org.junit.jupiter.api.Test;

import static com.jobscape.entitymanager.model.ApplicationStatus.APPLIED;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ApplicationMapperTest {

    private final ApplicationMapper mapper = new ApplicationMapper(new ApplicationStepMapper());

    @Test
    void mapsApplicationSteps() {
        User user = new User();
        user.setId(1L);

        Company company = new Company();
        company.setId(2L);

        Application application = new Application();
        application.setUser(user);
        application.setCompany(company);

        ApplicationStep step = new ApplicationStep();
        step.setId(3L);
        step.setStepStatus(APPLIED);
        application.getSteps().add(step);

        ApplicationDto dto = mapper.toDto(application);

        assertEquals(1, dto.getSteps().size());
        assertEquals(3L, dto.getSteps().get(0).getId());
        assertEquals(APPLIED, dto.getSteps().get(0).getStepStatus());
    }
}
