package com.project.trackfit.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.springframework.security.core.userdetails.UserDetails;

@MappedSuperclass
public abstract class BaseUser implements UserDetails{
    @Column(nullable = false, unique = true)
    private  String email;
    private  String password;
    private  byte[] storedHash;
    private  byte[] storedSalt;


    public byte[] getStoredHash() {
        return storedHash;
    }

    public void setStoredHash(byte[] storedHash) {
        this.storedHash = storedHash;
    }

    public byte[] getStoredSalt() {
        return storedSalt;
    }

    public void setStoredSalt(byte[] storedSalt) {
        this.storedSalt = storedSalt;
    }




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
