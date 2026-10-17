package com.jobscape.applicationservice.controller;

import com.jobscape.applicationservice.dto.CreateApplicationRequest;
import jakarta.validation.Valid;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ControllerContractTest {

    @Test
    void applicationRoutesShareOneBasePathAndValidateCreateRequest() throws NoSuchMethodException {
        RequestMapping mapping = ApplicationController.class.getAnnotation(RequestMapping.class);
        Method create = ApplicationController.class.getDeclaredMethod(
                "createApplication",
                CreateApplicationRequest.class
        );

        assertArrayEquals(new String[]{"/application-service/applications"}, mapping.value());
        assertTrue(create.getParameters()[0].isAnnotationPresent(Valid.class));
    }

    @Test
    void applicationStepControllerExposesDeleteRoute() throws NoSuchMethodException {
        Method delete = ApplicationStepController.class.getDeclaredMethod("delete", Long.class);
        DeleteMapping mapping = delete.getAnnotation(DeleteMapping.class);

        assertArrayEquals(new String[]{"/delete/{id}"}, mapping.value());
    }
}
