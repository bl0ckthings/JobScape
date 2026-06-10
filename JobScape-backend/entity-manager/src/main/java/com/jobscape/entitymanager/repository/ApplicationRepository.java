package com.jobscape.entitymanager.repository;

import com.jobscape.entitymanager.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByUserId(Long id);
    void deleteById(Long id);
}
