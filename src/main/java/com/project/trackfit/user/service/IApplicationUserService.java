package com.project.trackfit.user.service;

import com.project.trackfit.user.dto.CreateUserRequest;

import java.util.UUID;

public interface IApplicationUserService {
    UUID createUser(CreateUserRequest createUserRequest);
}
