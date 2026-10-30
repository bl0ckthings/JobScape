package com.jobscape.webgateway.service.applicationDto;

import lombok.Data;

@Data
public class ApplicationDetailRequest {
    private Long applicationId;
    private Long userId;
}
