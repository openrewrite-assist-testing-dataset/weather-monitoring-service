package com.weather.api.auth;

import java.security.Principal;

public class UserPrincipal implements Principal {
    private final String name;
    private final String type;

    public UserPrincipal(String name, String type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "UserPrincipal{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}