package com.jobscape.companyservice.service;

import com.jobscape.companyservice.client.CompanyClient;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {

        private final CompanyClient companyClient;



}
