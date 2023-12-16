package com.project.trackfit.user;

import java.util.UUID;

public interface IApplicationUserService {
    UUID createUser(CreateUserRequest createUserRequest);
}
