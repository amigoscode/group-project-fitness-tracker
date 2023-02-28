package com.project.trackfit.service.impl;

import com.project.trackfit.exception.UserDoesNotExistException;
import com.project.trackfit.model.User;
import com.project.trackfit.repository.UserRepository;
import com.project.trackfit.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImplementation implements UserService {

    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long userId) throws UserDoesNotExistException {
        Optional<User> optionalUser;
        try {
            optionalUser = userRepository.findById(userId);
        } catch (NoSuchElementException e) {
            throw new UserDoesNotExistException("");
        }
        return optionalUser.get();
    }
}
