package com.jobscape.webgateway.service.dashboardDto;


public record DashboardActivityDto(
        Integer timeline,
        Long nombreCandidatures,
        Long nombreReponses,
        Long nombreEntretien,
        Long nombreOffre
) {}
