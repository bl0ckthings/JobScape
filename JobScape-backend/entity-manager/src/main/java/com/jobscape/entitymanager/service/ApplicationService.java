package com.jobscape.entitymanager.service;

import com.jobscape.entitymanager.dto.ApplicationDto;
import com.jobscape.entitymanager.exception.ResourceNotFoundException;
import com.jobscape.entitymanager.model.Application;
import com.jobscape.entitymanager.model.Company;
import com.jobscape.entitymanager.model.User;
import com.jobscape.entitymanager.repository.ApplicationRepository;
import com.jobscape.entitymanager.repository.CompanyRepository;
import com.jobscape.entitymanager.repository.UserRepository;
import com.jobscape.entitymanager.service.mapper.ApplicationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {


    private final ApplicationRepository applicationRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;


    private final ApplicationMapper applicationMapper;

    @Transactional(readOnly = true)
    public ApplicationDto getById(Long id) {
        return applicationMapper.toDto(findApplication(id));
    }

    @Transactional(readOnly = true)
    public List<ApplicationDto> getAll() {
        return applicationRepository.findAll()
                .stream()
                .map(applicationMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ApplicationDto> getByUserId(Long userId) {
        return applicationRepository.findByUserId(userId)
                .stream()
                .map(applicationMapper::toDto)
                .toList();
    }

    @Transactional
    public ApplicationDto create(ApplicationDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", dto.getUserId())
                );

        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Company", dto.getCompanyId())
                );

        return applicationMapper.toDto(applicationRepository.save(applicationMapper.toEntity(dto, user, company)));
    }

    @Transactional
    public void delete(Long id) {
        applicationRepository.delete(findApplication(id));
    }

    @Transactional
    public ApplicationDto update(
            Long id,
            ApplicationDto updatedApplication
    ) {
        Application existing = findApplication(id);

        if (updatedApplication.getJobTitle() != null) {
            existing.setJobTitle(updatedApplication.getJobTitle());
        }

        if (updatedApplication.getCity() != null) {
            existing.setCity(updatedApplication.getCity());
        }

        if (updatedApplication.getCountry() != null) {
            existing.setCountry(updatedApplication.getCountry());
        }

        if (updatedApplication.getSource() != null) {
            existing.setSource(updatedApplication.getSource());
        }

        if (updatedApplication.getUrl() != null) {
            existing.setUrl(updatedApplication.getUrl());
        }

        if (updatedApplication.getStatus() != null) {
            existing.setStatus(updatedApplication.getStatus());
        }

        if (updatedApplication.getWorkMode() != null) {
            existing.setWorkMode(updatedApplication.getWorkMode());
        }

        if (updatedApplication.getUserId() != null) {
            existing.setUser(userRepository.findById(updatedApplication.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", updatedApplication.getUserId())));
        }

        if (updatedApplication.getCompanyId() != null) {
            existing.setCompany(companyRepository.findById(updatedApplication.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company", updatedApplication.getCompanyId())));
        }


        return applicationMapper.toDto(applicationRepository.save(existing));
    }

    private Application findApplication(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application", id));
    }
}
