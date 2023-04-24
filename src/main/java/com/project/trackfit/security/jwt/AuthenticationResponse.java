package com.project.trackfit.security.jwt;

import java.io.Serializable;

public record AuthenticationResponse(String token) implements Serializable { }