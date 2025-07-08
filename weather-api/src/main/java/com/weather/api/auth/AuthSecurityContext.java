package com.weather.api.auth;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class AuthSecurityContext implements SecurityContext {
    private final User principal;
    private final String scheme;
    private final ContainerRequestContext requestContext;

    public AuthSecurityContext(User principal, String scheme, ContainerRequestContext requestContext) {
        this.principal = principal;
        this.scheme = scheme;
        this.requestContext = requestContext;
    }

    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    @Override
    public boolean isUserInRole(String role) {
        return true;
    }

    @Override
    public boolean isSecure() {
        return requestContext.getSecurityContext().isSecure();
    }

    @Override
    public String getAuthenticationScheme() {
        return scheme;
    }
}