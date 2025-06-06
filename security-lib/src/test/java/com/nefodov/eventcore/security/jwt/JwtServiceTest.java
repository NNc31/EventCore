package com.nefodov.eventcore.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        String secret = "very-long-test-value-for-very-long-jwt-secret-very-long-test-value-for-very-long-jwt-secret-very-long-test-value-for-very-long-jwt";
        jwtService = new JwtService(secret);
    }

    @Test
    void generateAndParseTokenSuccess() {
        String token = jwtService.generateAccessToken("test", Collections.singletonList("ROLE_ADMIN"));
        assertNotNull(token);

        Claims claims = jwtService.extractAllClaims(token);
        assertEquals("test", claims.getSubject());
        assertEquals(List.of("ROLE_ADMIN"), claims.get("roles"));
    }

    @Test
    void parseTokenFailure() {
        assertThrows(JwtException.class, () -> jwtService.extractAllClaims("invalid.token.here"));
    }
}
