package com.jobscape.applicationservice.dto.dashboard;


public record DashboardActivityDto(
        Integer timeline,
        Long nombreCandidatures,
        Long nombreReponses,
        Long nombreEntretien,
        Long nombreOffre
) {}
