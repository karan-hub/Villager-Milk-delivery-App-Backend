package com.karan.village_milk_app.Repositories;

import com.karan.village_milk_app.model.RefreshToken;
import com.karan.village_milk_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}