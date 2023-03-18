package com.project.trackfit.core;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public interface ApplicationUserService {
    UUID createUser(CreateUserRequest createUserRequest) throws NoSuchAlgorithmException;

}
