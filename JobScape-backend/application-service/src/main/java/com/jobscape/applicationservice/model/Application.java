package com.jobscape.applicationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Application {

    private Long id;

    private String jobTitle;
    private String city;
    private String country;
    private String source;
    private String url;
    private ApplicationStatus status;
    private String workMode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private User user;

    private Company company;


    private List<ApplicationStep> steps = new ArrayList<>();
}
