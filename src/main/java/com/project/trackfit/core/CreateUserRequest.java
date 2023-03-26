package com.project.trackfit.core;

public record CreateUserRequest(
        String email,
        String password,
        String firstName,
        String lastName,
        Role role,
        Integer age,
        String address
) { }