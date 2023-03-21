package com.project.trackfit.security.jwt;

import java.io.Serializable;

public record AuthenticationRequest(String email, String password) implements Serializable {
}