package com.project.trackfit.service.impl;

import com.project.trackfit.exception.EmailNotValidException;
import com.project.trackfit.exception.ResourceNotFoundException;
import com.project.trackfit.model.User;
import com.project.trackfit.registration.EmailValidator;
import com.project.trackfit.repository.UserRepository;
import com.project.trackfit.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final EmailValidator emailValidator;

    @Override
    public User createUser(User user) {
        if (!(emailValidator.checkMailPattern(user.getEmail()))){
            throw new EmailNotValidException();
        }
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(ResourceNotFoundException::new);
    }
}
