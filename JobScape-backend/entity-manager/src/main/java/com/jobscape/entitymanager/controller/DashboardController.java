package com.jobscape.entitymanager.controller;

import com.jobscape.entitymanager.dto.DashboardActivityDto;
import com.jobscape.entitymanager.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;


    @GetMapping("/stats")
    public List<DashboardActivityDto> getStats(
            @RequestParam(required = false) Integer week,
            @RequestParam(required = false) Integer month,
            @RequestParam Integer year,
            @RequestParam Long userId
    ) {
        return dashboardService.getDashboardActivity(
                week, month, year, userId
        );
    }

}
