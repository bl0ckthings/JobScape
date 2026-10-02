package com.jobscape.applicationservice.service;

import com.jobscape.applicationservice.client.ApplicationClient;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class ApplicationService {

    private ApplicationClient applicationClient;


}
