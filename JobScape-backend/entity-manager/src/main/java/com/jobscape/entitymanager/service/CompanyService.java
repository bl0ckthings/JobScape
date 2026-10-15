package com.jobscape.entitymanager.service;

import com.jobscape.entitymanager.dto.CompanyDto;
import com.jobscape.entitymanager.exception.ResourceNotFoundException;
import com.jobscape.entitymanager.model.Company;
import com.jobscape.entitymanager.repository.CompanyRepository;
import com.jobscape.entitymanager.service.mapper.CompanyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Transactional(readOnly = true)
    public List<CompanyDto> getAll() {
        return companyRepository.findAll().stream()
                .map(companyMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public CompanyDto getById(Long id) {
        return companyMapper.toDto(findCompany(id));
    }

    @Transactional
    public CompanyDto create(CompanyDto dto) {
        return companyMapper.toDto(companyRepository.save(companyMapper.toEntity(dto)));
    }

    @Transactional
    public CompanyDto update(Long id, CompanyDto dto) {
        Company company = findCompany(id);
        if (dto.getName() != null) {
            company.setName(dto.getName());
        }
        if (dto.getWebsite() != null) {
            company.setWebsite(dto.getWebsite());
        }
        return companyMapper.toDto(companyRepository.save(company));
    }

    @Transactional
    public void delete(Long id) {
        companyRepository.delete(findCompany(id));
    }

    private Company findCompany(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company", id));
    }
}
