package com.jobscape.entitymanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContactSummaryDto {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String role;
}
