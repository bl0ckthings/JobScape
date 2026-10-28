package com.jobscape.webgateway.service.contactDto;

import com.jobscape.webgateway.service.companyDto.CompanySummaryDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
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
