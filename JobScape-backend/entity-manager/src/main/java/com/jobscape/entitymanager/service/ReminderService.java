package com.jobscape.entitymanager.service;

import com.jobscape.entitymanager.dto.ReminderDto;
import com.jobscape.entitymanager.exception.ResourceNotFoundException;
import com.jobscape.entitymanager.model.Application;
import com.jobscape.entitymanager.model.Reminder;
import com.jobscape.entitymanager.repository.ApplicationRepository;
import com.jobscape.entitymanager.repository.ReminderRepository;
import com.jobscape.entitymanager.service.mapper.ReminderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final ReminderRepository reminderRepository;
    private final ApplicationRepository applicationRepository;
    private final ReminderMapper reminderMapper;

    @Transactional(readOnly = true)
    public ReminderDto getById(Long id) {
        return reminderMapper.toDto(findReminder(id));
    }

    @Transactional(readOnly = true)
    public List<ReminderDto> getAll() {
        return reminderRepository.findAll().stream()
                .map(reminderMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReminderDto> getByApplicationId(Long applicationId) {
        return reminderRepository
                .findByApplication_IdOrderByReminderDateAscIdAsc(applicationId)
                .stream()
                .map(reminderMapper::toDto)
                .toList();
    }

    @Transactional
    public ReminderDto create(ReminderDto dto) {
        Application application = findApplication(dto.getApplicationId());
        return reminderMapper.toDto(reminderRepository.save(reminderMapper.toEntity(dto, application)));
    }

    @Transactional
    public ReminderDto update(Long id, Long userId, ReminderDto dto) {
        // Le propriétaire fait partie de la condition d'écriture ; le rappel garde sa candidature.
        int updated = reminderRepository.updateOwnedReminder(
                id, userId, dto.getReminderDate(), dto.getComment(), LocalDateTime.now());
        if (updated == 0) {
            throw new ResourceNotFoundException("Reminder", id);
        }
        return reminderMapper.toDto(findReminder(id));
    }

    @Transactional
    public void delete(Long id, Long userId) {
        if (reminderRepository.deleteOwnedReminder(id, userId) == 0) {
            throw new ResourceNotFoundException("Reminder", id);
        }
    }

    private Reminder findReminder(Long id) {
        return reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder", id));
    }

    private Application findApplication(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("applicationId is required");
        }
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application", id));
    }
}
