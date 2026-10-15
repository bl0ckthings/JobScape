package com.jobscape.entitymanager.service;

import com.jobscape.entitymanager.dto.ApplicationStepDto;
import com.jobscape.entitymanager.exception.ResourceNotFoundException;
import com.jobscape.entitymanager.model.Application;
import com.jobscape.entitymanager.model.ApplicationStep;
import com.jobscape.entitymanager.model.Contact;
import com.jobscape.entitymanager.repository.ApplicationRepository;
import com.jobscape.entitymanager.repository.ApplicationStepRepository;
import com.jobscape.entitymanager.repository.ContactRepository;
import com.jobscape.entitymanager.service.mapper.ApplicationStepMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationStepService {
    private final ApplicationStepRepository applicationStepRepository;
    private final ApplicationRepository applicationRepository;
    private final ContactRepository contactRepository;
    private final ApplicationStepMapper applicationStepMapper;

    @Transactional(readOnly = true)
    public ApplicationStepDto getById(Long id) {
        return applicationStepMapper.toDto(findStep(id));
    }

    @Transactional(readOnly = true)
    public List<ApplicationStepDto> getAll() {
        return applicationStepRepository.findAll().stream()
                .map(applicationStepMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ApplicationStepDto> getByApplicationId(Long applicationId) {
        return applicationStepRepository.findByApplicationId(applicationId).stream()
                .map(applicationStepMapper::toDto)
                .toList();
    }

    @Transactional
    public ApplicationStepDto create(ApplicationStepDto dto) {
        Application application = findApplication(dto.getApplicationId());
        Contact contact = findOptionalContact(dto.getContactId());
        return applicationStepMapper.toDto(applicationStepRepository.save(
                applicationStepMapper.toEntity(dto, application, contact)
        ));
    }

    @Transactional
    public ApplicationStepDto update(Long id, ApplicationStepDto dto) {
        ApplicationStep step = findStep(id);
        if (dto.getComment() != null) {
            step.setComment(dto.getComment());
        }
        if (dto.getStepDate() != null) {
            step.setStepDate(dto.getStepDate());
        }
        if (dto.getStepStatus() != null) {
            step.setStepStatus(dto.getStepStatus());
        }
        if (dto.getApplicationId() != null) {
            step.setApplication(findApplication(dto.getApplicationId()));
        }
        if (dto.getContactId() != null) {
            step.setContact(findOptionalContact(dto.getContactId()));
        }
        return applicationStepMapper.toDto(applicationStepRepository.save(step));
    }

    @Transactional
    public void delete(Long id) {
        applicationStepRepository.delete(findStep(id));
    }

    private ApplicationStep findStep(Long id) {
        return applicationStepRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application step", id));
    }

    private Application findApplication(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("applicationId is required");
        }
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application", id));
    }

    private Contact findOptionalContact(Long id) {
        if (id == null) {
            return null;
        }
        return contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact", id));
    }
}
