package com.weather.api.auth;

import io.dropwizard.auth.Authenticator;

import java.util.List;
import java.util.Optional;

public class ApiKeyAuthenticator implements Authenticator<String, String> {
    private final List<String> validApiKeys;

    public ApiKeyAuthenticator(List<String> validApiKeys) {
        this.validApiKeys = validApiKeys;
    }

    @Override
    public Optional<String> authenticate(String apiKey) {
        if (validApiKeys.contains(apiKey)) {
            return Optional.of("api-user");
        }
        return Optional.empty();
    }
}