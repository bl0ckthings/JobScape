package com.jobscape.applicationservice.service;

import com.jobscape.applicationservice.client.DashboardClient;
import com.jobscape.applicationservice.client.UsersClient;
import com.jobscape.applicationservice.dto.dashboard.DashboardActivityDto;
import com.jobscape.applicationservice.dto.dashboard.DashboardResponse;
import com.jobscape.applicationservice.dto.dashboard.DashboardStat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardClient dashboardClient;
    private final UsersClient usersClient;

    public DashboardResponse getDashboardStats(
            Integer week,
            Integer month,
            Integer year,
            Long userId
    ) {

       List<DashboardActivityDto> stats = dashboardClient.getDashboardStats(week, month, year, userId);
        long applications = stats.stream()
                .mapToLong(DashboardActivityDto::nombreCandidatures)
                .sum();

        long responses = stats.stream()
                .mapToLong(DashboardActivityDto::nombreReponses)
                .sum();

        long interviews = stats.stream()
                .mapToLong(DashboardActivityDto::nombreEntretien)
                .sum();

        long offers = stats.stream()
                .mapToLong(DashboardActivityDto::nombreOffre)
                .sum();

        String firstName = usersClient.getUserById(userId).getFirstName();
        return new DashboardResponse(
                firstName,
                new DashboardStat(applications, null),
                new DashboardStat(responses, null),
                new DashboardStat(interviews, null),
                new DashboardStat(offers, null),
                stats
        );
    }
}
