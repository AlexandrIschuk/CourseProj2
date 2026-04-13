package ru.ssau.carshwebcourse.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;
import ru.ssau.carshwebcourse.filters.JwtFilter;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService("testSecretKeyForJwt1234567890");
    }

    @Test
    void testGenerateToken_ShouldReturnValidToken() throws Exception {
        UserDetails userDetails = User.builder()
                .username("test@example.com")
                .password("password")
                .authorities(List.of())
                .build();

        String token = tokenService.generateToken(userDetails);

        assertNotNull(token);
        assertTrue(token.split("\\.").length == 2);
    }

    @Test
    void testGetDecodePayload_ShouldReturnCorrectEmail() throws Exception {
        UserDetails userDetails = User.builder()
                .username("test@example.com")
                .password("password")
                .authorities(List.of())
                .build();

        String token = tokenService.generateToken(userDetails);
        Map<String, Object> payload = tokenService.getDecodePayload(token);

        assertEquals("test@example.com", payload.get("email"));
    }

    @Test
    void testGenerateRefreshToken_ShouldReturnValidToken() throws Exception {
        UserDetails userDetails = User.builder()
                .username("test@example.com")
                .password("password")
                .authorities(List.of())
                .build();

        String refreshToken = tokenService.generateRefreshToken(userDetails);

        assertNotNull(refreshToken);
    }
}
