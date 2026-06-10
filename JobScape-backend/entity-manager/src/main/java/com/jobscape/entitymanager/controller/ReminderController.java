package com.jobscape.entitymanager.controller;


import com.jobscape.entitymanager.model.Reminder;
import com.jobscape.entitymanager.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reminders")
public class ReminderController {

    @Autowired
    ReminderRepository reminderRepository;

    @GetMapping("/applications/{id}")
    public List<Reminder> getReminderByApplication(@PathVariable Long id) {
        return reminderRepository.findByApplicationId(id);
    }

    @GetMapping("/{id}")
    public Reminder getReminderById(@PathVariable Long id) {
        return reminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));
    }

    @PostMapping("/create")
    public Reminder createReminder(@RequestBody Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    @DeleteMapping("/delete")
    public void deleteReminderById(@RequestParam Long id) {
        reminderRepository.deleteById(id);
    }
}
