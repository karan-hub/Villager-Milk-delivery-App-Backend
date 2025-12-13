package com.karan.village_milk_app.Controller;

import com.karan.village_milk_app.Request.OtpRequestDto;
import com.karan.village_milk_app.Response.OtpVerifyDto;
import com.karan.village_milk_app.Dto.TokenResponse;
import com.karan.village_milk_app.Dto.UserDTO;
import com.karan.village_milk_app.Repositories.RefreshTokenRepository;
import com.karan.village_milk_app.Repositories.UserRepository;
import com.karan.village_milk_app.Security.CookieService;
import com.karan.village_milk_app.Security.JwtService;
import com.karan.village_milk_app.Service.AuthService;
import com.karan.village_milk_app.Service.OtpService;
import com.karan.village_milk_app.model.RefreshToken;
import com.karan.village_milk_app.model.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth/otp")
@RequiredArgsConstructor
public class OtpAuthController {

    private final OtpService otpService;
    private final AuthService authService;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieService cookieService;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @PostMapping("/request")
    public ResponseEntity<Void> requestOtp(@RequestBody OtpRequestDto dto) {
        otpService.requestOtp(dto.phone());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<TokenResponse> verifyOtp(
            @RequestBody OtpVerifyDto dto,
            HttpServletResponse response
    ) {
        UserDTO userDto = otpService.verifyOtp(dto.phone(), dto.otp());

        User user = userRepository.findByPhone(dto.phone())
                .orElseThrow(() -> new RuntimeException("User not found after OTP verification"));

        String jti = UUID.randomUUID().toString();

        RefreshToken refreshTokenOb = RefreshToken.builder()
                .jti(jti)
                .user(user)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshTokenOb);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user, refreshTokenOb.getJti());

        cookieService.attachRefreshCookie(response, refreshToken, (int) jwtService.getRefreshTtlSeconds());
        cookieService.addNoStoreHeaders(response);

        TokenResponse tokenResponse = TokenResponse.of(accessToken, refreshToken, jwtService.getAccessTtlSeconds(), userDto);
        return ResponseEntity.ok(tokenResponse);
    }
}

