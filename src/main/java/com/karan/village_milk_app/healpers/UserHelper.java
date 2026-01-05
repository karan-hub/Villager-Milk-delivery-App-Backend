package com.karan.village_milk_app.healpers;

import com.karan.village_milk_app.Exceptions.UnauthorizedException;
import com.karan.village_milk_app.Repositories.UserRepository;
import com.karan.village_milk_app.model.User;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public final class UserHelper {

    private UserHelper() {
        // utility class
    }

    public static UUID parseUUID(String value) throws BadRequestException {
        if (value == null || value.isBlank()) {
            throw new BadRequestException("UUID must not be null or empty");
        }

        try {
            return UUID.fromString(value.trim());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid UUID format: " + value);
        }
    }

    public static User getCurrentUser(UserRepository userRepository) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();

        String phone;

        if (principal instanceof UserDetails userDetails) {
            phone = userDetails.getUsername();
        } else if (principal instanceof String s) {
            phone = s;
        } else {
            throw new UnauthorizedException("Invalid authentication principal");
        }

        return userRepository.findByPhone(phone)
                .orElseThrow(() ->
                        new UnauthorizedException("Authenticated user not found"));
    }
}
