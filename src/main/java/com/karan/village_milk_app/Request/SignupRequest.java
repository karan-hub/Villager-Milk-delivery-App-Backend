package com.karan.village_milk_app.Request;

import lombok.Data;

@Data
public class SignupRequest {
    private String name;
    private String phone;
    private String password;
}
