package com.karan.village_milk_app.Service;

import com.karan.village_milk_app.model.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenService {
    RefreshToken save(RefreshToken token);
    Optional<RefreshToken> findByJti(String jti);
    void revoke(String jti);
}