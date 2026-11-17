package com.jobscape.applicationservice.client;

import com.jobscape.applicationservice.dto.user.UserResponse;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("user-service")
public interface UsersClient {

    @GetMapping("users/{id}")
    UserResponse getUserById(@PathVariable Long id);

}
