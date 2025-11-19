package com.karan.village_milk_app.Controller;




import com.karan.village_milk_app.Dto.AddressDTO;
import com.karan.village_milk_app.Dto.AddressMapper;
import com.karan.village_milk_app.Dto.UserDTO;
import com.karan.village_milk_app.Dto.UserMapper;
import com.karan.village_milk_app.Repositories.UserRepository;
import com.karan.village_milk_app.model.Address;
import com.karan.village_milk_app.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private String getCurrentPhone() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            return ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
        }
        return String.valueOf(principal);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getProfile() {
        String phone = getCurrentPhone();
        User user = userRepository.findByPhone(phone);
        if (user == null) return ResponseEntity.status(404).body(Map.of("error","User not found"));
        // Don't return password

        UserDTO dto = UserMapper.toDTO(user);

        return ResponseEntity.ok(dto);
    }

    @Transactional
    @PutMapping("/me/address")
    public ResponseEntity<?> updateAddress(@RequestBody AddressDTO dto) {

        String phone = getCurrentPhone();
        User user = userRepository.findByPhone(phone);

        if (user == null)
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));

        Address address = user.getAddress();

        if (address == null) {
            address = new Address();
            address.setUser(user); // VERY IMPORTANT
        }

        AddressMapper.updateEntity(address, dto);

        user.setAddress(address);
        userRepository.save(user);

        return ResponseEntity.ok(AddressMapper.toDTO(address));
    }


    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String,String> payload) {
        String oldPassword = payload.get("oldPassword");
        String newPassword = payload.get("newPassword");
        if (oldPassword == null || newPassword == null)
            return ResponseEntity.badRequest().body(Map.of("error","oldPassword & newPassword required"));

        String phone = getCurrentPhone();
        User user = userRepository.findByPhone(phone);
        if (user == null) return ResponseEntity.status(404).body(Map.of("error","User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword()))
            return ResponseEntity.status(401).body(Map.of("error","Invalid current password"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message","Password updated"));
    }
}
