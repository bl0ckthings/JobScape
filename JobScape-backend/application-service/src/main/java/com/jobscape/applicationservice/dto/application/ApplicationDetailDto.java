package com.jobscape.applicationservice.dto.application;

import com.jobscape.applicationservice.dto.contact.ContactSummaryDto;
import com.jobscape.applicationservice.dto.reminder.ReminderDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ApplicationDetailDto {
    private ApplicationDto application; // contient déjà les étapes
    private List<ContactSummaryDto> contacts = new ArrayList<>();
    private List<ReminderDto> reminders = new ArrayList<>();
}
