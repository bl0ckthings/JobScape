package com.jobscape.entitymanager.service;

import com.jobscape.entitymanager.dto.DashboardActivityDto;
import com.jobscape.entitymanager.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    @Transactional(readOnly = true)
    public List<DashboardActivityDto> getDashboardActivity(
            Integer week,
            Integer month,
            Integer year,
            Long userId
           ) {
        return dashboardRepository.getStats(
                week,
                month,
                year,
                userId
        );
    }
}
