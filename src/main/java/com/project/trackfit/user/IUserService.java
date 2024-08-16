package com.project.trackfit.user;

import java.util.UUID;

public interface IUserService {
    UUID createUser(UserCreationRequest userCreationRequest);
}
