package com.jobscape.applicationservice.dto.applicationstep;

import com.jobscape.applicationservice.model.ApplicationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CreateApplicationStepRequest {
    private ApplicationStatus stepStatus;
    private LocalDateTime stepDate;
    private String comment;
    private Long contactId;
}
