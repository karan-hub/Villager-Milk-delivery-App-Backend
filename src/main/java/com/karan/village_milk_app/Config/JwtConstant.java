package com.karan.village_milk_app.Config;



public class JwtConstant {
    // Keep this secret safe and load from env in production
    public static final String SECRET_KEY = "karanjitujadugarrahulrahulbhavadunanalakhanlukhhavairi";
    public static final String JWT_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    // 10 days in milliseconds (adjust as needed)
    public static final long EXPIRATION_MS = 10L * 24 * 60 * 60 * 1000;
}

