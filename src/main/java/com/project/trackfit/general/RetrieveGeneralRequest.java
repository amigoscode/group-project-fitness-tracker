package com.project.trackfit.general;

import java.util.UUID;

public record RetrieveGeneralRequest(
        UUID id,
        String firstName,
        String lastName,
        Integer age,
        String email,
        String address
) {
}
