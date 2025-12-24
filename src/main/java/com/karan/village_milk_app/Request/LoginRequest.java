package com.karan.village_milk_app.Request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class LoginRequest {
    private  String phone;
    private  String password;

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }
}
