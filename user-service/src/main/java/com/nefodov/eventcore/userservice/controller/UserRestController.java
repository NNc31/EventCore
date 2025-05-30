package com.nefodov.eventcore.userservice.controller;

import com.nefodov.eventcore.userservice.model.User;
import com.nefodov.eventcore.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserRestController {

    private final UserService userService;

    public UserRestController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return new ArrayList<>();
    }
}
