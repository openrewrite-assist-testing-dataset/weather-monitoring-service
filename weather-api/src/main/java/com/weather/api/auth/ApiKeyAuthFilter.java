package com.weather.api.auth;

import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.Authenticator;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import java.io.IOException;
import java.util.Optional;

@Priority(Priorities.AUTHENTICATION)
public class ApiKeyAuthFilter extends AuthFilter<String, User> {

    private ApiKeyAuthFilter() {}

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        final String apiKey = requestContext.getHeaders().getFirst("X-API-Key");

        if (apiKey == null) {
            throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix, realm));
        }

        try {
            final Optional<User> result = authenticator.authenticate(apiKey);
            if (result.isPresent()) {
                requestContext.setSecurityContext(new AuthSecurityContext(result.get(), prefix, requestContext));
                return;
            }
        } catch (Exception e) {
            logger.warn("Error authenticating API key", e);
        }

        throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix, realm));
    }

    public static ApiKeyAuthFilter createFilter(Authenticator<String, User> authenticator, String prefix, String realm) {
        ApiKeyAuthFilter filter = new ApiKeyAuthFilter();
        filter.authenticator = authenticator;
        filter.prefix = prefix;
        filter.realm = realm;
        return filter;
    }
}