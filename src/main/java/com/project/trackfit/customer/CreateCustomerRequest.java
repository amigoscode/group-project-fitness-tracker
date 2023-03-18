package com.project.trackfit.customer;

public record CreateCustomerRequest(
        String firstName,
        String lastName,
        Integer age,
        String email,
        String address
) { }