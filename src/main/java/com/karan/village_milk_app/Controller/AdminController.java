package com.karan.village_milk_app.Controller;

import com.karan.village_milk_app.Dto.UserDTO;
import com.karan.village_milk_app.Repositories.UserRepository;
import com.karan.village_milk_app.Service.UserService;
import com.karan.village_milk_app.model.Type.Role;
import com.karan.village_milk_app.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private  final UserRepository userRepository;
    private final UserService userService;


    @PutMapping("/admin/users/{id}/make-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public void makeAdmin(@PathVariable UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow();
        user.setRole(Role.ROLE_ADMIN);
        userRepository.save(user);
        List<User> all = userRepository.findAll();
        System.out.println(all);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "0") int page , @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(userService.getAllUsers(page , size));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO created = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/phone/{phone}")
    @PreAuthorize(" hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserByPhone(@PathVariable String phone) {
        return ResponseEntity.ok(userService.getUserByPhone(phone));
    }

}
