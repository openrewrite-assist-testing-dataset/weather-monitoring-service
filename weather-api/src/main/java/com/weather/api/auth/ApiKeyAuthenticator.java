package com.weather.api.auth;

import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.List;
import java.util.Optional;

public class ApiKeyAuthenticator implements Authenticator<BasicCredentials, UserPrincipal> {
    private final List<String> validApiKeys;

    public ApiKeyAuthenticator(List<String> validApiKeys) {
        this.validApiKeys = validApiKeys;
    }

    @Override
    public Optional<UserPrincipal> authenticate(BasicCredentials credentials) {
        // Use username as the API key
        String username = credentials.getUsername();
        if (username != null && validApiKeys.contains(username)) {
            return Optional.of(new UserPrincipal(username, "api-key"));
        }
        return Optional.empty();
    }
}