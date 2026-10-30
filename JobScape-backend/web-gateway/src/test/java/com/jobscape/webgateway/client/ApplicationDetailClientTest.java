package com.jobscape.webgateway.client;

import feign.Feign;
import feign.Response;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ApplicationDetailClientTest {
    @Test void detailUsesGetAndTransmitsAuthenticatedIdentity() {
        ApplicationClient client = Feign.builder().contract(new SpringMvcContract())
                .decoder((response, type) -> null)
                .client((request, options) -> {
                    assertEquals("GET", request.httpMethod().name());
                    assertEquals("http://application-service/application-service/applications/12/detail?userId=7", request.url());
                    return Response.builder().request(request).status(200).reason("OK")
                            .headers(Map.of()).body("{}", java.nio.charset.StandardCharsets.UTF_8).build();
                }).target(ApplicationClient.class, "http://application-service");
        client.getApplicationsDetail(12L, 7L);
    }
}
