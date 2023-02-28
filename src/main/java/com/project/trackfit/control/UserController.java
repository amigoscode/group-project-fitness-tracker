package com.project.trackfit.control;

import com.project.trackfit.model.User;
import com.project.trackfit.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    /**
     * Spring Boot REST API creates a User
     * http://[::1]:8080/api/v1/users/
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User savedUser = userService.createUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    /**
     * Spring Boot REST API gets a User by Id
     * http://[::1]:8080/api/v1/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long userId) {
        User user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}