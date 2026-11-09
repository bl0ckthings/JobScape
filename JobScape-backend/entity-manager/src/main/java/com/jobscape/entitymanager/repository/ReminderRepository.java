package com.jobscape.entitymanager.repository;

import com.jobscape.entitymanager.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    @Query(value = """
            SELECT r.*
            FROM reminders r
            WHERE r.application_id = :applicationId
            ORDER BY r.reminder_date ASC, r.id ASC
            """, nativeQuery = true)
    List<Reminder> findByApplicationId(@Param("applicationId") Long applicationId);

    // Sans valeur, COALESCE garde le champ actuel.
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            UPDATE reminders
            SET reminder_date = COALESCE(:reminderDate, reminder_date),
                comment = COALESCE(:comment, comment), updated_at = :updatedAt
            WHERE id = :id AND application_id IN
                (SELECT a.id FROM applications a WHERE a.user_id = :userId)
            """, nativeQuery = true)
    int updateOwnedReminder(@Param("id") Long id, @Param("userId") Long userId,
                            @Param("reminderDate") LocalDateTime reminderDate,
                            @Param("comment") String comment, @Param("updatedAt") LocalDateTime updatedAt);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            DELETE FROM reminders
            WHERE id = :id AND application_id IN
                (SELECT a.id FROM applications a WHERE a.user_id = :userId)
            """, nativeQuery = true)
    int deleteOwnedReminder(@Param("id") Long id, @Param("userId") Long userId);
}
