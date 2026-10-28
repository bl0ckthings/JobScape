package com.jobscape.entitymanager.repository;

import com.jobscape.entitymanager.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> getCompanyByDomain(String domain);
    List<Company> findTop10ByNameContainingIgnoreCaseOrderByNameAsc(String name);
}
