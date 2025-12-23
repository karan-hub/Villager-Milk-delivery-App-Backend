package com.karan.village_milk_app.Config;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecurityEndpoints {


    public static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/api/v1/auth/**",
            "/api/v1/products/**",
            "/api/v1/subscription-plans/**",
            "/error",
            "/error/**"
    );


    public static final List<String> USER_ENDPOINTS = List.of(
            "/api/v1/subscriptions/**",
            "/api/v1/orders/**",
            "/api/v1/user/**"
    );

    public static final List<String> ADMIN_ENDPOINTS = List.of(
            "/api/v1/admin/**",
            "/api/v1/dashboard/**"
    );
}

