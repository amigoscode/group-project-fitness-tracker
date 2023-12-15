package com.project.trackfit.core;

import com.project.trackfit.user.CreateUserRequest;

import java.util.UUID;

public interface IApplicationUserService {
    UUID createUser(CreateUserRequest createUserRequest);
}
