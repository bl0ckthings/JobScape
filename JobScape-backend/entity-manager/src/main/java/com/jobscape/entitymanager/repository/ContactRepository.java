package com.jobscape.entitymanager.repository;

import com.jobscape.entitymanager.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByCompanyId(Long id);
    List<Contact> findByUserId(Long id);
    @Query(value = """
            SELECT c.*
            FROM contacts c
            WHERE c.company_id = :companyId AND c.user_id = :userId
            ORDER BY c.created_at ASC, c.id ASC
            """, nativeQuery = true)
    List<Contact> findOwnedByCompanyId(
            @Param("companyId") Long companyId, @Param("userId") Long userId);
}
