package com.karan.village_milk_app.Service;




import com.karan.village_milk_app.Repositories.UserRepository;
import com.karan.village_milk_app.model.Type.Role;
import com.karan.village_milk_app.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.phone:9999999999}")
    private String adminPhone;

    @Value("${app.admin.password:Admin@123}")
    private String adminPassword;

    public AdminSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByPhone(adminPhone)) {
            User admin = new User();
            admin.setName("Admin");
            admin.setPhone(adminPhone);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ROLE_ADMIN);
            userRepository.save(admin);
            System.out.println("Default admin created -> phone: " + adminPhone);
        } else {
            System.out.println("Admin already exists.");
        }
    }
}
