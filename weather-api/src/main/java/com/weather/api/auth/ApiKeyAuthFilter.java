package com.weather.api.auth;

import io.dropwizard.auth.AuthFilter;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import java.io.IOException;
import java.util.Optional;

@Priority(Priorities.AUTHENTICATION)
public class ApiKeyAuthFilter<P> extends AuthFilter<String, P> {
    
    private ApiKeyAuthFilter() {}

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        final String apiKey = requestContext.getHeaders().getFirst("X-API-Key");
        
        if (apiKey == null) {
            throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix, realm));
        }

        try {
            final Optional<P> result = authenticator.authenticate(apiKey);
            if (result.isPresent()) {
                requestContext.setSecurityContext(new AuthSecurityContext(result.get(), scheme, requestContext));
                return;
            }
        } catch (Exception e) {
            logger.warn("Error authenticating API key", e);
        }

        throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix, realm));
    }

    public static class Builder<P> extends AuthFilter.Builder<String, P, ApiKeyAuthFilter<P>> {
        @Override
        protected ApiKeyAuthFilter<P> newInstance() {
            return new ApiKeyAuthFilter<>();
        }
    }
}