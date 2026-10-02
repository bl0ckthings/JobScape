package com.jobscape.companyservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    private Long id;

    private String name;

    private String website;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<Contact> contacts = new ArrayList<>();
}
