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
        String token = jwtService.generateToken("test", Collections.singletonList("ROLE_ADMIN"), null);
        assertNotNull(token);

        Claims claims = jwtService.extractAllClaims(token);
        assertEquals("test", claims.getSubject());
        assertEquals(List.of("ROLE_ADMIN"), claims.get("roles"));
    }

    @Test
    void generateAndParseTokenWithExtraClaimsSuccess() {
        Map<String, String> map = new HashMap<>();
        map.put("extra", "value");
        map.put("extra2", "value2");
        String token = jwtService.generateToken("test", Collections.singletonList("ROLE_USER"), map);
        assertNotNull(token);
        System.out.println(token);

        Claims claims = jwtService.extractAllClaims(token);
        assertEquals("test", claims.getSubject());
        assertEquals("value", claims.get("extra"));
        assertEquals("value2", claims.get("extra2"));
        assertEquals(List.of("ROLE_USER"), claims.get("roles"));
    }

    @Test
    void parseTokenFailure() {
        assertThrows(JwtException.class, () -> jwtService.extractAllClaims("invalid.token.here"));
    }
}
