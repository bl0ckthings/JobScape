package com.jobscape.entitymanager.dto;


public record DashboardActivityDto(
        Integer timeline,
        Long nombreCandidatures,
        Long nombreReponses,
        Long nombreEntretien,
        Long nombreOffre
) {}
