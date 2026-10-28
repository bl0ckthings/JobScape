package com.jobscape.webgateway.controller;

import com.jobscape.webgateway.client.ApplicationClient;
import com.jobscape.webgateway.service.applicationDto.ApplicationDto;
import com.jobscape.webgateway.service.applicationDto.CreateApplicationRequest;
import com.jobscape.webgateway.service.applicationDto.ApplicationCategory;
import com.jobscape.webgateway.service.applicationDto.ApplicationsByStatusResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ApplicationControllerTest {

    @Test
    void returnsCompanyNameAndLogoOnBoardApplications() throws Exception {
        ApplicationClient client = mock(ApplicationClient.class);
        ApplicationDto application = new ApplicationDto();
        application.setId(1L);
        application.setCompanyName("Acme");
        application.setCompanyDomain("acme.com");
        application.setCompanyLogoUrl("https://example.com/acme.png");
        ApplicationsByStatusResponse response = new ApplicationsByStatusResponse();
        response.setApplicationsByStatus(Map.of(ApplicationCategory.APPLIED, List.of(application)));
        when(client.getApplicationsByUserId(42L)).thenReturn(response);
        MockMvc mvc = MockMvcBuilders.standaloneSetup(new ApplicationController(client)).build();

        mvc.perform(get("/api/application/user").requestAttr("authenticatedUserId", 42L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationsByStatus.APPLIED[0].companyName").value("Acme"))
                .andExpect(jsonPath("$.applicationsByStatus.APPLIED[0].companyDomain").value("acme.com"))
                .andExpect(jsonPath("$.applicationsByStatus.APPLIED[0].companyLogoUrl").value("https://example.com/acme.png"));
    }

    @Test
    void createsApplicationForAuthenticatedUserRatherThanBodyUserId() throws Exception {
        ApplicationClient client = mock(ApplicationClient.class);
        when(client.createApplication(any(CreateApplicationRequest.class))).thenReturn(new ApplicationDto());
        MockMvc mvc = MockMvcBuilders.standaloneSetup(new ApplicationController(client)).build();

        mvc.perform(post("/api/application/create")
                        .requestAttr("authenticatedUserId", 42L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"companyName":"JobScape","jobTitle":"Developer","city":"Paris",
                                 "country":"France","source":"LinkedIn","workMode":"Remote",
                                 "initialStatus":"APPLIED","userId":999}
                                """))
                .andExpect(status().isOk());

        ArgumentCaptor<CreateApplicationRequest> request = ArgumentCaptor.forClass(CreateApplicationRequest.class);
        verify(client).createApplication(request.capture());
        assertEquals(42L, request.getValue().getUserId());
        assertEquals("Developer", request.getValue().getJobTitle());
    }
}
