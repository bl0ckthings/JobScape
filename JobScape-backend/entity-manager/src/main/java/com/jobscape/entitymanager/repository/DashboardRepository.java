package com.jobscape.entitymanager.repository;

import com.jobscape.entitymanager.dto.DashboardActivityDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DashboardRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<DashboardActivityDto> getStats(
            Integer semaine,
            Integer mois,
            Integer annee,
            Long userId
    ) {
        return jdbcTemplate.query(
                "CALL sp_statistics(?, ?, ?, ?)",
                (rs, rowNum) -> new DashboardActivityDto(
                        rs.getInt("timeline"),
                        rs.getLong("nombreCandidatures"),
                        rs.getLong("nombreReponses"),
                        rs.getLong("nombreEntretien"),
                        rs.getLong("nombreOffre")
                ),
                semaine, mois, annee, userId
        );
    }
}
