package com.jobscape.entitymanager.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@RequiredArgsConstructor
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
