package com.jobscape.webgateway.client;

import com.jobscape.webgateway.service.dashboardDto.DashboardActivityDto;
import com.jobscape.webgateway.service.dashboardDto.DashboardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "application-service", contextId = "dashboardClient")
public interface DashboardClient {

    @GetMapping("/application-service/dashboard/stats")
    DashboardResponse getDashboardStats(
            @RequestParam("week") Integer week,
            @RequestParam("month") Integer month,
            @RequestParam("year") Integer year,
            @RequestParam("userId") Long userId
    );
}
