package com.karan.village_milk_app.healpers;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class OtpGenerator {

    private final SecureRandom random = new SecureRandom();

    public String generate() {
        return String.valueOf(100000 + random.nextInt(900000));
    }
}

