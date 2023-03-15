package com.project.trackfit.customer;

public record CreateCustomerRequest(

        String firstName,

        String lastName,
        Integer age,
        String address,
        String email,

        String password




        ) {
}
