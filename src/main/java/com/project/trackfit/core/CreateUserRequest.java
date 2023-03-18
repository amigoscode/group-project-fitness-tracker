package com.project.trackfit.core;

public record CreateUserRequest(
        String email,
        String password,
        String firstName,
        String lastName,
        String  role
) {
}
