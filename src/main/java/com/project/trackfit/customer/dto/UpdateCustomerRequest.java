package com.project.trackfit.customer.dto;

import com.project.trackfit.user.component.Role;

public record UpdateCustomerRequest(
        Integer age,
        String address,
        Role role
) {
}
