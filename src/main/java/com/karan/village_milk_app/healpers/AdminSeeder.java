package com.karan.village_milk_app.healpers;

import com.karan.village_milk_app.Repositories.UserRepository;
import com.karan.village_milk_app.Service.UserService;
import com.karan.village_milk_app.model.Type.Role;
import com.karan.village_milk_app.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {
    private  final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.existsByPhone("9999999999")) return;
        User admin = new User();
        admin.setName("Super Admin");
        admin.setPhone("9999999999");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ROLE_ADMIN);
        admin.setEnable(true);
        userRepository.save(admin);
        System.out.println("ADMIN USER CREATED");

    }
}
