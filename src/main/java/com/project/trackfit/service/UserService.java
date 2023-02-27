package com.project.trackfit.service;

import com.project.trackfit.model.User;

public interface UserService {
    User createUser(User user);
    User getUserById(Long userId);
}
