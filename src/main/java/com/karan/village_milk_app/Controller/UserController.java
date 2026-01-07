package com.karan.village_milk_app.Controller;




 
import com.karan.village_milk_app.Dto.UserDTO;
import com.karan.village_milk_app.Repositories.UserRepository;
import com.karan.village_milk_app.Service.UserService;
import com.karan.village_milk_app.healpers.UserHelper;
import com.karan.village_milk_app.model.Type.Role;
import com.karan.village_milk_app.model.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId) throws BadRequestException {
        UUID currentUserId = getCurrentUserId();
        UUID requestedId = UserHelper.parseUUID(userId);
        if (!currentUserId.equals(requestedId) && !isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(userService.getUserById(userId));
    }



    @PutMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable String userId) throws BadRequestException {
        UUID currentUserId = getCurrentUserId();
        UUID requestedId = UserHelper.parseUUID(userId);
        if (!currentUserId.equals(requestedId) && !isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UserDTO updated = userService.updateUser(userDTO, userId);
        return ResponseEntity.ok(updated);
    }

    private UUID getCurrentUserId() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }

    private boolean isAdmin() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getRole() == Role.ROLE_ADMIN;
    }

}
