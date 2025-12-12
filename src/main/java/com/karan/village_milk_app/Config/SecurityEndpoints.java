package com.karan.village_milk_app.Config;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecurityEndpoints {
    public final List<String> PUBLIC_ENDPOINTS = List.of(
            "/api/v1/auth/*",
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/api/v1/auth/refresh",
            "/api/v1/auth/logout"
    );

    // Admin-only endpoints
    public final List<String> ADMIN_ENDPOINTS = List.of(
            "/api/v1/admin/**",
            "/api/v1/dashboard/**"
    );
}
