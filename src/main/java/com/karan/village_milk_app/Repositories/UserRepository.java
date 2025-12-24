package com.karan.village_milk_app.Repositories;

import com.karan.village_milk_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByPhone(String phone);
    boolean existsByPhone(String phone);

    long countByRole(String role);
    long countByCreatedAtAfter(LocalDateTime dateTime);
}