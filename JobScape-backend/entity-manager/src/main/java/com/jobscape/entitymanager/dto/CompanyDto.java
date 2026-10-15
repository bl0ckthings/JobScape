package com.jobscape.entitymanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CompanyDto {
    private Long id;
    private String name;
    private String website;
    private List<ContactSummaryDto> contacts = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
