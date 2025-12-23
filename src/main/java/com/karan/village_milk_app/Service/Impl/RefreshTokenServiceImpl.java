package com.karan.village_milk_app.Service.Impl;

import com.karan.village_milk_app.Repositories.RefreshTokenRepository;
import com.karan.village_milk_app.Service.RefreshTokenService;
import com.karan.village_milk_app.model.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken save(RefreshToken token) {
        return refreshTokenRepository.save(token);
    }

    @Override
    public Optional<RefreshToken> findByJti(String jti) {
        return refreshTokenRepository.findByJti(jti);
    }

    @Override
    public void revoke(String jti) {
        refreshTokenRepository.findByJti(jti).ifPresent(token -> {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        });
    }
}