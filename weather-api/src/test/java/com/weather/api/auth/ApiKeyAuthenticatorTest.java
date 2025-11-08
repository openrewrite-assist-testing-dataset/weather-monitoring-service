package com.weather.api.auth;

import io.dropwizard.auth.basic.BasicCredentials;
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
        BasicCredentials validCredentials = new BasicCredentials("key1", "password");

        // When
        Optional<User> result = apiKeyAuthenticator.authenticate(validCredentials);

        // Then
        assertTrue(result.isPresent());
        assertEquals("key1", result.get().getName());
        assertEquals("api-key", result.get().getType());
    }

    @Test
    void authenticate_ReturnsEmpty_WhenApiKeyIsInvalid() {
        // Given
        BasicCredentials invalidCredentials = new BasicCredentials("invalid-key", "password");

        // When
        Optional<User> result = apiKeyAuthenticator.authenticate(invalidCredentials);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void authenticate_ReturnsEmpty_WhenApiKeyIsNull() {
        // Given - BasicCredentials doesn't allow null username, so we test with invalid key
        BasicCredentials invalidCredentials = new BasicCredentials("null-key", "password");

        // When
        Optional<User> result = apiKeyAuthenticator.authenticate(invalidCredentials);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void authenticate_ReturnsEmpty_WhenApiKeyIsEmpty() {
        // Given
        BasicCredentials emptyCredentials = new BasicCredentials("", "password");

        // When
        Optional<User> result = apiKeyAuthenticator.authenticate(emptyCredentials);

        // Then
        assertFalse(result.isPresent());
    }
}