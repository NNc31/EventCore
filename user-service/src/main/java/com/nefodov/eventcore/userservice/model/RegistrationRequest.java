package com.nefodov.eventcore.userservice.model;

public record RegistrationRequest(String username, String password, String email) {
}
