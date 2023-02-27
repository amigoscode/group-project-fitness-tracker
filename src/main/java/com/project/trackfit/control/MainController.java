package com.project.trackfit.control;

import com.project.trackfit.model.User;
import com.project.trackfit.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/registrations")
public class MainController {

    private UserService userService;

    /**
     * Spring Boot REST API creates a User
     * http://[::1]:8080/api/v1/registrations/
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User savedUser = userService.createUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
}