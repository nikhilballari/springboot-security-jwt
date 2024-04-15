package com.dashboardapi.demo.controller;

import com.dashboardapi.demo.entity.User;
import com.dashboardapi.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/users")
    public User addNewUser(@RequestBody User user) {
        return userService.addNewUser(user);
    }
}
