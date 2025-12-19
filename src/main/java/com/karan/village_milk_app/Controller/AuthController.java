package com.karan.village_milk_app.Controller;


import com.karan.village_milk_app.Dto.RefreshTokenRequest;
import com.karan.village_milk_app.Dto.TokenResponse;
import com.karan.village_milk_app.Dto.UserDTO;
import com.karan.village_milk_app.Exceptions.ResourceNotFoundException;
import com.karan.village_milk_app.Request.LoginRequest;
import com.karan.village_milk_app.Request.SignupRequest;
import com.karan.village_milk_app.Repositories.OtpCodeRepository;
import com.karan.village_milk_app.Repositories.RefreshTokenRepository;
import com.karan.village_milk_app.Repositories.UserRepository;
import com.karan.village_milk_app.Security.CookieService;
import com.karan.village_milk_app.Security.JwtService;
import com.karan.village_milk_app.Service.AuthService;
import com.karan.village_milk_app.model.RefreshToken;
import com.karan.village_milk_app.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

     private final UserRepository userRepository;
     private final PasswordEncoder passwordEncoder;
     private final JwtService jwtService;
     private final RefreshTokenRepository refreshTokenRepository;
     private final OtpCodeRepository otpCodeRepository;
     private final CookieService cookieService;
     private final ModelMapper modelMapper;
     private final AuthenticationManager authenticationManager;
     private final AuthService authService;



    // -----------------------------------
    //             LOGIN
    // -----------------------------------

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest  request , HttpServletResponse response) {

        Authentication authentication = authenticate(request);
        User user = (User) authentication.getPrincipal();
        String jti = UUID.randomUUID().toString();

        RefreshToken refreshTokenOb = RefreshToken.builder()
                .jti(jti)
                .user(user)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshTokenOb);

        String accessToken =jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user , refreshTokenOb.getJti());

        cookieService.attachRefreshCookie(response , refreshToken , (int) jwtService.getRefreshTtlSeconds());
        cookieService.addNoStoreHeaders(response);

        TokenResponse tokenResponse = TokenResponse.of(accessToken, refreshToken, jwtService.getAccessTtlSeconds(), modelMapper.map(user, UserDTO.class));
        return  ResponseEntity.ok(tokenResponse);
    }

    private Authentication authenticate(LoginRequest request) {

        try {
             return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getPhone(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new ResourceNotFoundException("Invalid phone or password");
        }

    }

    // -----------------------------------
    //             LOG-OUT
    // -----------------------------------

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request , HttpServletResponse response) {
        readRefreshTokenFromRequest(null ,request).ifPresent(token->{
            try {
                if (jwtService.isRefreshToken(token)){
                    String jit = jwtService.getJti(token);
                    refreshTokenRepository.findByJti(jit)
                            .ifPresent(refreshToken -> {
                                refreshToken.setRevoked(true);
                                refreshTokenRepository.save(refreshToken);
                            });
                }
            } catch (Exception ignored) { }

        });

        cookieService.clearRefreshCookie(response);
        cookieService.addNoStoreHeaders(response);
        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.NO_CONTENT ).build();

    }


    // -----------------------------------
    //             REFRESH
    // -----------------------------------

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody( required = false) RefreshTokenRequest body , HttpServletResponse response , HttpServletRequest request){

        String refreshToken = readRefreshTokenFromRequest(body , request)
                .orElseThrow(()-> new BadCredentialsException("Invalid Refresh Token"));

        if (!jwtService.isRefreshToken(refreshToken))
            throw  new BadCredentialsException("Invalid Refresh Token Type ");

        String jti = jwtService.getJti(refreshToken);
        UUID userId = jwtService.getUserId(refreshToken);

        RefreshToken storedRefreshToken = refreshTokenRepository.findByJti(jti).orElseThrow(() -> new BadCredentialsException("Invalid Refresh Token"));

        if (storedRefreshToken.isRevoked())
            throw  new BadCredentialsException("Refresh Token Expired or  Revoked");

        if (storedRefreshToken.getExpiresAt().isBefore(Instant.now()))
            throw  new BadCredentialsException("Refresh Token Expired ");

        if (!storedRefreshToken.getUser().getId().equals(userId))
            throw  new BadCredentialsException("Refresh Token Doest Belongs To This User ");

        storedRefreshToken.setRevoked(true);
        String  newJit = UUID.randomUUID().toString();
        storedRefreshToken.setReplacedByToken(newJit);
        refreshTokenRepository.save(storedRefreshToken);


        User user = storedRefreshToken.getUser();
        var newRefreshTokenOb = RefreshToken.builder()
                .jti(newJit)
                .user(user)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
                .revoked(false)
                .build();

        refreshTokenRepository.save(newRefreshTokenOb);

        String newAccessToken = jwtService.generateAccessToken(user);
        String  newRefreshToken = jwtService.generateRefreshToken(user, newRefreshTokenOb.getJti());

        cookieService.attachRefreshCookie(response,newRefreshToken , (int) jwtService.getRefreshTtlSeconds());
        cookieService.addNoStoreHeaders(response);

        return  ResponseEntity.ok(TokenResponse.of(
                newAccessToken ,
                newRefreshToken,
                jwtService.getAccessTtlSeconds(),
                modelMapper.map(user , UserDTO.class)
        ));

    }

    // ------------------ OTP: send code -------------------



    // ------------------ OTP: verify & login/register -------------------


    @PostMapping("/register")
    public ResponseEntity<UserDTO>  registerUser(@RequestBody SignupRequest  signupRequest){

        return  ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(signupRequest));
    }


    private Optional<String> readRefreshTokenFromRequest(
            RefreshTokenRequest body,
            HttpServletRequest request
    ) {
        // 1. From cookie
        if (request.getCookies() != null) {
            Optional<String> fromCookie = Arrays.stream(request.getCookies())
                    .filter(c -> cookieService.getRefreshTokenCookieName().equals(c.getName()))
                    .map(Cookie::getValue)
                    .filter(v -> !v.isBlank())
                    .findFirst();
            if (fromCookie.isPresent()) {
                return fromCookie;
            }
        }

        // 2. From body
        if (body != null && body.refreshToken() != null && !body.refreshToken().isBlank()) {
            return Optional.of(body.refreshToken());
        }

        // 3. Custom header
        String refreshHeader = request.getHeader("X-Refresh-Token");
        if (refreshHeader != null && !refreshHeader.isBlank()) {
            return Optional.of(refreshHeader.trim());
        }

        // 4. From Authorization header (Bearer ...)
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.regionMatches(true, 0, "Bearer", 0, 6)) {
            String candidate = authHeader.substring(7).trim();
            if (!candidate.isEmpty()) {
                try {
                    if (jwtService.isRefreshToken(candidate)) {
                        return Optional.of(candidate);
                    }
                } catch (Exception ignored) { }
            }
        }

        return Optional.empty();
    }

}




















































































































































































































































