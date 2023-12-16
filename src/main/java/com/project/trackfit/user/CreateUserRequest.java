package com.project.trackfit.user;

import com.project.trackfit.core.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateUserRequest {

    @Email(message = "Email is invalid")
    @NotBlank(message = "Email must not be blank")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "First Name is required")
    private String firstName;
    @NotBlank(message = "Last Name is required")
    private String lastName;
    @NotNull(message = "Role cannot be null")
    private Role role;
    @NotNull(message = "Age cannot be null")
    private Integer age;
    @NotBlank(message = "Address is required")
    private String address;

    public CreateUserRequest(String email,
                             String password,
                             String firstName,
                             String lastName,
                             Role role,
                             Integer age,
                             String address) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.age = age;
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}