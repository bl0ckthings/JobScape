package com.jobscape.reminderservice.controller;

import com.jobscape.reminderservice.dto.CreateReminderRequest;
import com.jobscape.reminderservice.dto.ReminderDto;
import com.jobscape.reminderservice.dto.UpdateReminderRequest;
import com.jobscape.reminderservice.service.ReminderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reminder")
@RequiredArgsConstructor
public class ReminderController {
    private final ReminderService reminderService;

    @GetMapping("/{id}")
    public ReminderDto getReminderById(@PathVariable Long id) {

        return reminderService.getReminderById(id);
    }

    @GetMapping("/application/{id}")
    public List<ReminderDto> getRemindersByApplicationId(@PathVariable Long id) {

        return reminderService.getRemindersByApplicationId(id);
    }

    @PostMapping("/create")
    public ReminderDto createReminder(@Valid @RequestBody CreateReminderRequest createReminderRequest) {
        return reminderService.createReminder(createReminderRequest);
    }

    @PutMapping("/update/{id}")
    public ReminderDto updateReminder(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReminderRequest updateRequest
    ) {
        return reminderService.updateReminder(id, updateRequest);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteReminderById(@PathVariable Long id) {
        reminderService.deleteReminderById(id);
    }







}
