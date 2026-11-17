package com.jobscape.webgateway.service.dashboardDto;

import com.jobscape.webgateway.service.dashboardDto.DashboardActivityDto;

import java.util.List;

public record DashboardResponse(
    String firstName,
    DashboardStat applicationsSent,
    DashboardStat responsesReceived,
    DashboardStat interviews,
    DashboardStat offers,
    List<DashboardActivityDto> activity
) {}