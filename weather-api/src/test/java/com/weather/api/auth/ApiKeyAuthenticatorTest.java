package com.weather.api.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiKeyAuthenticatorTest {

    private ApiKeyAuthenticator apiKeyAuthenticator;
    private final List<String> validApiKeys = Arrays.asList("key1", "key2", "key3");

    @BeforeEach
    void setUp() {
        apiKeyAuthenticator = new ApiKeyAuthenticator(validApiKeys);
    }

    @Test
    void authenticate_ReturnsUser_WhenApiKeyIsValid() {
        // Given
        String validApiKey = "key1";

        // When
        Optional<User> result = apiKeyAuthenticator.authenticate(validApiKey);

        // Then
        assertTrue(result.isPresent());
        assertEquals("api-user", result.get().getName());
    }

    @Test
    void authenticate_ReturnsEmpty_WhenApiKeyIsInvalid() {
        // Given
        String invalidApiKey = "invalid-key";

        // When
        Optional<User> result = apiKeyAuthenticator.authenticate(invalidApiKey);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void authenticate_ReturnsEmpty_WhenApiKeyIsNull() {
        // Given
        String nullApiKey = null;

        // When
        Optional<User> result = apiKeyAuthenticator.authenticate(nullApiKey);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void authenticate_ReturnsEmpty_WhenApiKeyIsEmpty() {
        // Given
        String emptyApiKey = "";

        // When
        Optional<User> result = apiKeyAuthenticator.authenticate(emptyApiKey);

        // Then
        assertFalse(result.isPresent());
    }
}