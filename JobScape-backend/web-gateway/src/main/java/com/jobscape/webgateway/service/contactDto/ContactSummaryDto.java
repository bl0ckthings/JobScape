package com.jobscape.webgateway.service.contactDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContactSummaryDto {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String role;
}
