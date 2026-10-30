package com.jobscape.webgateway.service.applicationDto;

import com.jobscape.webgateway.service.contactDto.ContactSummaryDto;
import com.jobscape.webgateway.service.reminderDto.ReminderDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ApplicationDetailDto {
    private ApplicationDto application; // contient déjà les étapes
    private List<ContactSummaryDto> contacts = new ArrayList<>();
    private List<ReminderDto> reminders = new ArrayList<>();
}
