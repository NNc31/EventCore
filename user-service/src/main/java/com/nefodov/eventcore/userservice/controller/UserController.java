package com.nefodov.eventcore.userservice.controller;

import com.nefodov.eventcore.security.jwt.JwtService;
import com.nefodov.eventcore.userservice.model.JwtResponse;
import com.nefodov.eventcore.userservice.model.SignInRequest;
import com.nefodov.eventcore.userservice.model.SignUpRequest;
import com.nefodov.eventcore.userservice.model.User;
import com.nefodov.eventcore.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(@Autowired UserService userService, @Autowired JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request) {
        try {
            User user = userService.authenticateAndGetUser(request);
            String jwt = jwtService.generateToken(user.getUsername(), List.of(user.getRole()));
            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        if (userService.register(request)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("User service is alive!");
    }
}
