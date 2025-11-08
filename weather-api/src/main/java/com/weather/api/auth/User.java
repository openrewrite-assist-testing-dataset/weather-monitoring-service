package com.weather.api.auth;

import java.security.Principal;

public class User implements Principal {
    private final String name;
    private final String type;

    public User(String name, String type) {
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
        return "User{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}