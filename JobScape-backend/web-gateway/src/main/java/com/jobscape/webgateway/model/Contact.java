package com.jobscape.webgateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    private Long id;

    private String name;
    private String phone;
    private String email;
    private String role;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long companyId;
}