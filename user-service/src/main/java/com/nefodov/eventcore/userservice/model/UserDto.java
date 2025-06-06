package com.nefodov.eventcore.userservice.model;

import java.time.LocalDateTime;

public record UserDto(String username, String email, LocalDateTime createdAt) {
}
