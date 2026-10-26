package com.jobscape.webgateway.controller;

import com.jobscape.webgateway.client.ApplicationClient;
import com.jobscape.webgateway.service.applicationDto.ApplicationDto;
import com.jobscape.webgateway.service.applicationDto.CreateApplicationRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ApplicationControllerTest {

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
