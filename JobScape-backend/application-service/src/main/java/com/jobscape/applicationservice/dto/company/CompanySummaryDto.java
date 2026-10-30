package com.jobscape.applicationservice.dto.company;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompanySummaryDto {
    private Long id;
    private String name;
    private String website;
}
