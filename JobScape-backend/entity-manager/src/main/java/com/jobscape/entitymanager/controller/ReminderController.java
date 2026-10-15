package com.jobscape.entitymanager.controller;


import com.jobscape.entitymanager.dto.ReminderDto;
import com.jobscape.entitymanager.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/reminders")
@RequiredArgsConstructor
public class ReminderController {
    private final ReminderService reminderService;

    @GetMapping
    public List<ReminderDto> getAll() {
        return reminderService.getAll();
    }

    @GetMapping("/applications/{applicationId}")
    public List<ReminderDto> getReminderByApplication(@PathVariable Long applicationId) {
        return reminderService.getByApplicationId(applicationId);
    }

    @GetMapping("/{id}")
    public ReminderDto getReminderById(@PathVariable Long id) {
        return reminderService.getById(id);
    }

    @PostMapping("/create")
    public ReminderDto createReminder(@RequestBody ReminderDto reminder) {
        return reminderService.create(reminder);
    }

    @PutMapping("/update/{id}")
    public ReminderDto updateReminder(@PathVariable Long id, @RequestBody ReminderDto reminder) {
        return reminderService.update(id, reminder);
    }

    @DeleteMapping("/delete")
    public void deleteReminderById(@RequestParam Long id) {
        reminderService.delete(id);
    }
}
