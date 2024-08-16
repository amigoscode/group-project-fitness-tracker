package com.project.trackfit.user;

import com.project.trackfit.user.PasswordCreation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PasswordCreationTests {

    private PasswordCreation passwordCreation;

    @BeforeEach
    void setUp() {
        passwordCreation = new PasswordCreation();
    }

    @Test
    @DisplayName("Successful creation of a salt")
    void whenCreatingSalt_thenSuccess() {
        //when: creating a salt
        byte[] salt = passwordCreation.createSalt();

        //then: the expected salt should return
        assertNotNull(salt);
        assertEquals(128, salt.length);
    }

    @Test
    @DisplayName("Successful creation of two different salts")
    void whenCreateSalt_thenShouldGenerateUniqueSalts() {
        //when: creating two different salts
        byte[] salt1 = passwordCreation.createSalt();
        byte[] salt2 = passwordCreation.createSalt();

        //then: assert that salts are different
        assertNotEquals(Arrays.toString(salt1), Arrays.toString(salt2));
    }

    @Test
    @DisplayName("Create two hashes from the same password and check the equality of them")
    void givenCreationSalt_whenPasswordHashTwoTimes_thenShouldReturnConsistentHashes() {
        //given: a salt
        byte[] salt = passwordCreation.createSalt();

        //and: a test password
        String password = "TestPassword";

        //when: creating two hashes of the same password
        byte[] hash1 = passwordCreation.createPasswordHash(password, salt);
        byte[] hash2 = passwordCreation.createPasswordHash(password, salt);

        //then: the hashes should not be null and equal to each other
        assertNotNull(hash1);
        assertNotNull(hash2);
        assertArrayEquals(hash1, hash2);
    }

    @Test
    @DisplayName("Create two hashes from different passwords and check the non-equality of them")
    void createPasswordHash_WithDifferentPasswords_ShouldReturnDifferentHashes() {
        //given: a salt
        byte[] salt = passwordCreation.createSalt();

        //and: two test passwords
        String password1 = "Password1";
        String password2 = "Password2";

        //when: creating two hashes of these passwords
        byte[] hash1 = passwordCreation.createPasswordHash(password1, salt);
        byte[] hash2 = passwordCreation.createPasswordHash(password2, salt);

        //then: the hashes should not be null and non-equal to each other
        assertNotNull(hash1);
        assertNotNull(hash2);
        assertNotEquals(Arrays.toString(hash1), Arrays.toString(hash2));
    }
}
