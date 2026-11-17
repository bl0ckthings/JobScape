package com.jobscape.webgateway.controller;

import com.jobscape.webgateway.client.DashboardClient;
import com.jobscape.webgateway.service.dashboardDto.DashboardActivityDto;
import com.jobscape.webgateway.service.dashboardDto.DashboardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardClient dashboardClient;

    @GetMapping("/stats")
    public DashboardResponse getDashboardStats(
            @RequestParam(required = false) Integer week,
            @RequestParam(required = false) Integer month,
            @RequestParam Integer year,
            @RequestAttribute("authenticatedUserId") Long userId
    ) {
        return dashboardClient.getDashboardStats(week, month, year, userId);
    }

}
