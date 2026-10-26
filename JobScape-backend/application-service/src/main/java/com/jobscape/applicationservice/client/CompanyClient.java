package com.jobscape.applicationservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "company-service")
public interface CompanyClient {

}
