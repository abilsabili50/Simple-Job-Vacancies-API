package com.spring.core.entity;

public enum ERole {
    ADMIN("ADMIN"),
    SEEKER("SEEKER");

    private final String name;

    ERole(String role) {
        this.name = role;
    }
}
