package com.karan.village_milk_app.Config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "otp")
@Getter
@Setter
@Component
public class OtpProperties {
    private long expiryMinutes;
    private int maxAttempts;

    public long getExpiryMinutes() {
        return expiryMinutes;
    }
}
