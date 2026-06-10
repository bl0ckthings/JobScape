package com.jobscape.applicationservice.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@RequiredArgsConstructor
public class Contact {

    private Long id;

    private String name;
    private String phone;
    private String email;
    private String role;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    private Company company;


    private List<ApplicationStep> applicationSteps = new ArrayList<>();
}
