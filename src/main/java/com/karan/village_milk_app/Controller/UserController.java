package com.karan.village_milk_app.Controller;




 
import com.karan.village_milk_app.Dto.UserDTO;
import com.karan.village_milk_app.Service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO created = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @GetMapping("/{userId}")
    @PreAuthorize("#userId == authentication.name or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/phone/{phone}")
    @PreAuthorize("#phone == authentication.name or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserByPhone(@PathVariable String phone) {
        return ResponseEntity.ok(userService.getUserByPhone(phone));
    }


    @PutMapping("/{userId}")
    @PreAuthorize("#userId == authentication.name or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable String userId) {
        UserDTO updated = userService.updateUser(userDTO, userId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
