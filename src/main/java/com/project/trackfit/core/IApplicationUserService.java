package com.project.trackfit.core;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public interface IApplicationUserService {
    UUID createUser(CreateUserRequest createUserRequest);
}
