package com.example.finalproject.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class NurbolatDjumadilovJwtUtilTest {

    private NurbolatDjumadilovJwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new NurbolatDjumadilovJwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret",
                "test-secret-key-for-jwt-testing-purposes-only-32chars");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L);
    }

    @Test
    void generateToken_shouldReturnNonNullToken() {
        String token = jwtUtil.generateToken("user@test.com");
        assertThat(token).isNotNull().isNotBlank();
    }

    @Test
    void extractEmail_shouldReturnCorrectEmail() {
        String email = "user@test.com";
        String token = jwtUtil.generateToken(email);

        String extracted = jwtUtil.extractEmail(token);
        assertThat(extracted).isEqualTo(email);
    }

    @Test
    void isValid_shouldReturnTrue_forValidToken() {
        String token = jwtUtil.generateToken("user@test.com");
        assertThat(jwtUtil.isValid(token)).isTrue();
    }

    @Test
    void isValid_shouldReturnFalse_forInvalidToken() {
        assertThat(jwtUtil.isValid("invalid.token.here")).isFalse();
    }
}
