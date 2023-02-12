package com.project.trackfit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Customer {
    private int customer_id;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private String address;
}
