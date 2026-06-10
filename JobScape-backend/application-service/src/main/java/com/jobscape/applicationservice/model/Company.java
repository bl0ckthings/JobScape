package com.jobscape.applicationservice.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@RequiredArgsConstructor
public class Company {

    private Long id;
    private String name;
    private String website;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    private List<Contact> contacts = new ArrayList<>();

    public Company(String website, String name) {
        this.website = website;
        this.name = name;
    }
}
