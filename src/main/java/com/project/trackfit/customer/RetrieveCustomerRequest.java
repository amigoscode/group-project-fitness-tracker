package com.project.trackfit.customer;

public record RetrieveCustomerRequest(
        Long customer_id,
        String firstName,
        String LastName,
        String email,
        Integer age


) {
}
