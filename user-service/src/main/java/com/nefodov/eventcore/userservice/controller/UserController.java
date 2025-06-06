package com.nefodov.eventcore.userservice.controller;

import com.nefodov.eventcore.security.jwt.JwtService;
import com.nefodov.eventcore.userservice.model.*;
import com.nefodov.eventcore.userservice.service.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(@Autowired UserService userService, @Autowired JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        log.info("Sign up request for {}", request.username());
        if (userService.register(request)) {
            return ResponseEntity.ok().build();
        } else {
            log.warn("Bad sign up request for {}", request.username());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request, HttpServletResponse response) {
        log.info("Sign in request for {}", request.username());
        try {
            User user = userService.authenticateAndGetUser(request);
            String accessToken = jwtService.generateAccessToken(user.getUsername(), List.of(user.getRole()));
            String refreshToken = jwtService.generateRefreshToken(user.getUsername());

            Cookie cookie = new Cookie("refreshToken", refreshToken);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(cookie);

            return ResponseEntity.ok(new JwtResponse(accessToken));
        } catch (AuthenticationException e) {
            log.warn("Unauthorized sign in request for {}", request.username());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No refresh token");
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(c -> "refreshToken".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No refresh token");
        }

        try {
            String username = jwtService.extractUsername(refreshToken);
            if (!jwtService.validateToken(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
            }
            User user = userService.findByUsername(username);
            List<String> roles = List.of(user.getRole());
            String newAccessToken = jwtService.generateAccessToken(username, roles);
            return ResponseEntity.ok(new JwtResponse(newAccessToken));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
        }
    }

    @GetMapping("/account")
    public ResponseEntity<?> account() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(new UserDto(user.getUsername(), user.getEmail(), user.getCreatedAt()));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("permit all success");
    }

    @GetMapping("/test2")
    public ResponseEntity<String> testAuthorized() {
        return ResponseEntity.ok("authorized success");
    }
}
