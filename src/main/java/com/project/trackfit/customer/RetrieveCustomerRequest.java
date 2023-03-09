package com.project.trackfit.customer;

import java.util.UUID;

public record RetrieveCustomerRequest(
        UUID id,
        String firstName,
        String LastName,
        String email,
        Integer age
) {
}
