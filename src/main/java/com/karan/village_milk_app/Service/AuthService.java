package com.karan.village_milk_app.Service;

import com.karan.village_milk_app.Request.SignupRequest;
import com.karan.village_milk_app.Dto.UserDTO;

public interface AuthService {
    UserDTO registerUser(SignupRequest dto);
}
