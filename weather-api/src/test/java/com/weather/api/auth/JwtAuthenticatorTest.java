package com.weather.api.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtAuthenticatorTest {

    private JwtAuthenticator jwtAuthenticator;
    private final String secret = "test-secret-key";

    @BeforeEach
    void setUp() {
        jwtAuthenticator = new JwtAuthenticator(secret);
    }

    @Test
    void authenticate_ReturnsEmpty_WhenTokenIsInvalid() {
        // Given
        String invalidToken = "invalid.token.here";

        // When
        Optional<User> result = jwtAuthenticator.authenticate(invalidToken);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void authenticate_ReturnsEmpty_WhenTokenIsNull() {
        // Given
        String nullToken = null;

        // When
        Optional<User> result = jwtAuthenticator.authenticate(nullToken);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void authenticate_ReturnsEmpty_WhenTokenIsEmpty() {
        // Given
        String emptyToken = "";

        // When
        Optional<User> result = jwtAuthenticator.authenticate(emptyToken);

        // Then
        assertFalse(result.isPresent());
    }
}