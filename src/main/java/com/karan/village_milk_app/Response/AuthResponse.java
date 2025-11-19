package com.karan.village_milk_app.Response;

import com.karan.village_milk_app.model.Type.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String jwt;
    private Role role;
    private String message;
}

