package com.karan.village_milk_app.Controller;




 
import com.karan.village_milk_app.Dto.UserDTO;
import com.karan.village_milk_app.Repositories.UserRepository;
import com.karan.village_milk_app.Service.UserService;
import com.karan.village_milk_app.model.Type.Role;
import com.karan.village_milk_app.model.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


//    @GetMapping("/{userId}")
//    @PreAuthorize("#userId == authentication.name or hasRole('ADMIN')")
//    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId) {
//        return ResponseEntity.ok(userService.getUserById(userId));
//    }



    @PutMapping("/{userId}")
    @PreAuthorize("#userId == authentication.name or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable String userId) {
        UserDTO updated = userService.updateUser(userDTO, userId);
        return ResponseEntity.ok(updated);
    }


}
