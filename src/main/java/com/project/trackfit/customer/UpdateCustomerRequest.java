package com.project.trackfit.customer;

import com.project.trackfit.core.Role;

public record UpdateCustomerRequest(
        Integer age,
        String address,
        Role role

) {
}
