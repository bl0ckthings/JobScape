package com.jobscape.entitymanager.repository;

import com.jobscape.entitymanager.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByApplicationId(Long applicationId);
}
