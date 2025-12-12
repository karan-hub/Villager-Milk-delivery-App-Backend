package com.karan.village_milk_app.Service.Impl;

import com.karan.village_milk_app.Dto.UserDTO;
import com.karan.village_milk_app.Service.AuthService;
import com.karan.village_milk_app.Service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private  final UserService userService;
    private  final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO registerUser(UserDTO dto) {
        return  userService.createUser(dto);
    }
}
