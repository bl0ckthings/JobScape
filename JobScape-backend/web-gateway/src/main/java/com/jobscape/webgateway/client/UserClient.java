package com.jobscape.webgateway.client;

import com.jobscape.webgateway.service.authDto.LoginResponse;
import com.jobscape.webgateway.service.authDto.RegisterRequest;
import com.jobscape.webgateway.service.authDto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("user-service")
public interface UserClient {

    @RequestMapping(method = RequestMethod.GET, value = "/users/{id}", consumes = "application/json")
    UserResponse getUserById(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/users", consumes = "application/json")
    UserResponse getUser(@RequestParam String email);

    @RequestMapping(method = RequestMethod.GET, value = "/users/checkUser", consumes = "application/json")
    LoginResponse checkUser(@RequestParam String email);

    @RequestMapping(method = RequestMethod.POST, value = "/users/add", consumes = "application/json")
    UserResponse createUser(@RequestBody RegisterRequest user);

    @RequestMapping(method = RequestMethod.DELETE, value = "/users/delete/{id}", consumes = "application/json")
    void deleteUser(@PathVariable Long id);
}
