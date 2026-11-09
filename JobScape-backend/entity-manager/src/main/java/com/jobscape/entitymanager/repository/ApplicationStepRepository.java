package com.jobscape.entitymanager.repository;

import com.jobscape.entitymanager.model.ApplicationStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationStepRepository extends JpaRepository<ApplicationStep, Long> {
    @Query(value = """
            SELECT s.*
            FROM application_steps s
            WHERE s.application_id = :applicationId
            ORDER BY s.step_date DESC, s.id DESC
            """, nativeQuery = true)
    List<ApplicationStep> findByApplicationId(@Param("applicationId") Long applicationId);
}
