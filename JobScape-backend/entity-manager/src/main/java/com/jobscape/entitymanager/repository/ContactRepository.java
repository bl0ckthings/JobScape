package com.jobscape.entitymanager.repository;

import com.jobscape.entitymanager.model.ApplicationStep;
import com.jobscape.entitymanager.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByCompanyId(Long id);
}
