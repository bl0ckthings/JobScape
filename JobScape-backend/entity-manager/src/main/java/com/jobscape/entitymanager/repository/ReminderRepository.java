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
    List<Reminder> findByApplication_IdOrderByReminderDateAscIdAsc(
            Long applicationId
    );

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update Reminder r
            set r.reminderDate = coalesce(:reminderDate, r.reminderDate),
                r.comment = coalesce(:comment, r.comment), r.updatedAt = :updatedAt
            where r.id = :id and r.application.id in
                (select a.id from Application a where a.user.id = :userId)
            """)
    int updateOwnedReminder(@Param("id") Long id, @Param("userId") Long userId,
                            @Param("reminderDate") LocalDateTime reminderDate,
                            @Param("comment") String comment, @Param("updatedAt") LocalDateTime updatedAt);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            delete from Reminder r where r.id = :id and r.application.id in
                (select a.id from Application a where a.user.id = :userId)
            """)
    int deleteOwnedReminder(@Param("id") Long id, @Param("userId") Long userId);
}
