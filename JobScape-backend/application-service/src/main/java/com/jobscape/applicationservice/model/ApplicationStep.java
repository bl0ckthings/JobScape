package com.jobscape.applicationservice.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@Data
@RequiredArgsConstructor
public class ApplicationStep {

    private Long id;

    private String comment;

    private LocalDateTime stepDate;


    private ApplicationStatus stepStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Application application;

    private Contact contact;
}
