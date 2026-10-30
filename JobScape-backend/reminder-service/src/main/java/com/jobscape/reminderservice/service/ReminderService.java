package com.jobscape.reminderservice.service;

import com.jobscape.reminderservice.client.ReminderClient;
import com.jobscape.reminderservice.dto.CreateReminderRequest;
import com.jobscape.reminderservice.dto.ReminderDto;
import com.jobscape.reminderservice.dto.UpdateReminderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderClient reminderClient;

    public ReminderDto getReminderById(Long id) {
        return reminderClient.getReminderById(id);
    }

    public List<ReminderDto> getRemindersByApplicationId(Long applicationId) {
        return reminderClient.getRemindersByApplicationId(applicationId);
    }

    public ReminderDto createReminder(CreateReminderRequest request) {
        LocalDateTime reminderDate = request
                .getApplicationDate()
                .plusDays(7)
                .atTime(9,0);

        ReminderDto reminderDto = new ReminderDto();
        reminderDto.setApplicationId(request.getApplicationId());
        reminderDto.setReminderDate(reminderDate);
        reminderDto.setComment(request.getComment());
        return reminderClient.createReminder(reminderDto);
    }

    public ReminderDto updateReminder(Long id, Long userId, UpdateReminderRequest updateRequest) {
        return reminderClient.updateReminder(id, userId, updateRequest);
    }

    public void deleteReminderById(Long id, Long userId) {
        reminderClient.deleteReminder(id, userId);
    }

}
