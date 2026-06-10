package com.jobscape.user.client;

import com.jobscape.user.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("entity-manager")
public interface UserClient {
    @RequestMapping(method = RequestMethod.GET, value = "/data/users/{id}", consumes = "application/json")
    User findById(@PathVariable long id);

    @RequestMapping(method = RequestMethod.GET, value = "/data/users", consumes = "application/json")
    List<User> findByEmail(@RequestParam(required = false) String email);

    @RequestMapping(method = RequestMethod.POST, value = "/data/users/add", consumes = "application/json")
    User save(@RequestBody User user);

    @RequestMapping(method = RequestMethod.DELETE, value = "/data/users/delete", consumes = "application/json")
    void delete(@RequestBody User user);


}
