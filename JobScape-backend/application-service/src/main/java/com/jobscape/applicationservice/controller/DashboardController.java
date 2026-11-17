package com.jobscape.applicationservice.controller;

import com.jobscape.applicationservice.dto.dashboard.DashboardActivityDto;
import com.jobscape.applicationservice.dto.dashboard.DashboardResponse;
import com.jobscape.applicationservice.dto.dashboard.DashboardStat;
import com.jobscape.applicationservice.dto.user.UserResponse;
import com.jobscape.applicationservice.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/application-service/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public DashboardResponse getDashboardStats(
            @RequestParam(required = false) Integer week,
            @RequestParam(required = false) Integer month,
            @RequestParam Integer year,
            @RequestParam Long userId
    ) {
        return dashboardService.getDashboardStats(week, month, year, userId);
    }
}
