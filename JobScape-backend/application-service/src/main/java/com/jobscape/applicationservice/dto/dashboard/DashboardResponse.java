package com.jobscape.applicationservice.dto.dashboard;

import java.util.List;

public record DashboardResponse(
    String firstName,
    DashboardStat applicationsSent,
    DashboardStat responsesReceived,
    DashboardStat interviews,
    DashboardStat offers,
    List<DashboardActivityDto> activity
) {}