package com.jobscape.entitymanager.repository;

import com.jobscape.entitymanager.model.ApplicationStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationStepRepository extends JpaRepository<ApplicationStep, Long> {
    List<ApplicationStep> findByApplicationId(Long id);
}
