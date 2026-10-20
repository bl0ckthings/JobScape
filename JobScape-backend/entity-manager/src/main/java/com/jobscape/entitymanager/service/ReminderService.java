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
    public ReminderDto update(Long id, ReminderDto dto) {
        Reminder reminder = findReminder(id);
        if (dto.getReminderDate() != null) {
            reminder.setReminderDate(dto.getReminderDate());
        }
        if (dto.getComment() != null) {
            reminder.setComment(dto.getComment());
        }
        if (dto.getApplicationId() != null) {
            reminder.setApplication(findApplication(dto.getApplicationId()));
        }
        return reminderMapper.toDto(reminderRepository.save(reminder));
    }

    @Transactional
    public void delete(Long id) {
        reminderRepository.delete(findReminder(id));
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
