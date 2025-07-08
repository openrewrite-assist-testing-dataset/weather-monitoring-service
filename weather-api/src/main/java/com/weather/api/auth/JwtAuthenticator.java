package com.weather.api.auth;

import io.dropwizard.auth.Authenticator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

import java.util.Optional;

public class JwtAuthenticator implements Authenticator<String, String> {
    private final String secret;

    public JwtAuthenticator(String secret) {
        this.secret = secret;
    }

    @Override
    public Optional<String> authenticate(String token) {
        try {
            Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
            
            String username = claims.getSubject();
            if (username != null && !username.isEmpty()) {
                return Optional.of(username);
            }
        } catch (SignatureException e) {
            // Invalid token
        } catch (Exception e) {
            // Other JWT parsing errors
        }
        return Optional.empty();
    }
}