package com.jobscape.webgateway.client;

import com.jobscape.webgateway.model.User;
import com.jobscape.webgateway.service.dto.LoginRequest;
import com.jobscape.webgateway.service.dto.RegisterRequest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("users")
public interface UserClient {

    @RequestMapping(method = RequestMethod.GET, value = "/users/{id}", consumes = "application/json")
    User getUserById(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/users/", consumes = "application/json")
    List<User> getUser(@RequestParam String email);

    @RequestMapping(method = RequestMethod.POST, value = "/users/add", consumes = "application/json")
    User createUser(@RequestBody RegisterRequest user);

    @RequestMapping(method = RequestMethod.DELETE, value = "/users/delete/{id}", consumes = "application/json")
    void deleteUser(@PathVariable Long id);
}
