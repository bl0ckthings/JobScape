package com.jobscape.companyservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ContactDto {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private CompanySummaryDto company;
    private Long userId;
}
