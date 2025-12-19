package com.karan.village_milk_app.Service.Impl;

import com.karan.village_milk_app.Request.SignupRequest;
import com.karan.village_milk_app.Dto.UserDTO;
import com.karan.village_milk_app.Service.AuthService;
import com.karan.village_milk_app.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private  final UserService userService;
    private  final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO registerUser(SignupRequest dto) {
        return  userService.createUser(dto);
    }
}
