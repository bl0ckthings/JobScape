package com.jobscape.applicationservice.client;

import com.jobscape.applicationservice.dto.dashboard.DashboardActivityDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "entity-manager", contextId = "dashboardClient")
public interface DashboardClient {

    @GetMapping("/internal/dashboard/stats")
     List<DashboardActivityDto> getDashboardStats(
            @RequestParam(required = false) Integer week,
            @RequestParam(required = false) Integer month,
            @RequestParam Integer year,
            @RequestParam Long userId
    );
}
