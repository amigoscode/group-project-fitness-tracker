package com.project.trackfit.core.util;

import org.springframework.context.annotation.Bean;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SaltHelper {
    public byte [] createSalt(){
        var random = new SecureRandom();
        var salt =new byte[128];
        random.nextBytes(salt);
        return salt;

    }

    public byte[] createPasswordHash(String password , byte[]salt) throws NoSuchAlgorithmException{
        var md= MessageDigest.getInstance("SHA-512");
        md.update(salt);
        return md.digest(
                password.getBytes(StandardCharsets.UTF_8)
        );
    }

}
