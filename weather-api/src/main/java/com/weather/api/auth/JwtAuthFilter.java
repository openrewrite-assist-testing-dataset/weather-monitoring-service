package com.weather.api.auth;

import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.Authenticator;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Optional;

@Priority(Priorities.AUTHENTICATION)
public class JwtAuthFilter<P> extends AuthFilter<String, P> {
    
    private JwtAuthFilter() {}

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        final String header = requestContext.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        
        if (header == null) {
            throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix, realm));
        }

        final int space = header.indexOf(' ');
        if (space <= 0) {
            throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix, realm));
        }

        final String method = header.substring(0, space);
        if (!prefix.equalsIgnoreCase(method)) {
            throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix, realm));
        }

        final String token = header.substring(space + 1);
        
        try {
            final Optional<P> result = authenticator.authenticate(token);
            if (result.isPresent()) {
                requestContext.setSecurityContext(new AuthSecurityContext(result.get(), scheme, requestContext));
                return;
            }
        } catch (Exception e) {
            logger.warn("Error authenticating credentials", e);
        }

        throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix, realm));
    }

    public static class Builder<P> extends AuthFilter.Builder<String, P, JwtAuthFilter<P>> {
        @Override
        protected JwtAuthFilter<P> newInstance() {
            return new JwtAuthFilter<>();
        }
    }
}