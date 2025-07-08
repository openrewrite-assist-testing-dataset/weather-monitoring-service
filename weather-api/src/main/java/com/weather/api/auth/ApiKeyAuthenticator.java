package com.weather.api.auth;

import io.dropwizard.auth.Authenticator;

import java.util.List;
import java.util.Optional;

public class ApiKeyAuthenticator implements Authenticator<String, User> {
    private final List<String> validApiKeys;

    public ApiKeyAuthenticator(List<String> validApiKeys) {
        this.validApiKeys = validApiKeys;
    }

    @Override
    public Optional<User> authenticate(String apiKey) {
        if (validApiKeys.contains(apiKey)) {
            return Optional.of(new User("api-user"));
        }
        return Optional.empty();
    }
}