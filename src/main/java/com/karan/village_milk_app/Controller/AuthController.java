package com.karan.village_milk_app.Controller;


import com.karan.village_milk_app.Request.LoginRequest;
import com.karan.village_milk_app.Request.SignupRequest;
import com.karan.village_milk_app.Repositories.OtpCodeRepository;
import com.karan.village_milk_app.Repositories.RefreshTokenRepository;
import com.karan.village_milk_app.Config.JwtProvider;
import com.karan.village_milk_app.Repositories.UserRepository;
import com.karan.village_milk_app.Response.AuthResponse;
import com.karan.village_milk_app.Service.CustomUserDetailsService;
import com.karan.village_milk_app.model.OtpCode;
import com.karan.village_milk_app.model.RefreshToken;
import com.karan.village_milk_app.model.Type.Role;
import com.karan.village_milk_app.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtProvider jwtProvider;
    @Autowired private RefreshTokenRepository refreshTokenRepository;
    @Autowired private OtpCodeRepository otpCodeRepository;
    @Autowired private CustomUserDetailsService customUserDetailsService;
    @Autowired private AuthenticationManager authenticationManager;


// -----------------------------------
    //             SIGNUP
    // -----------------------------------

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody SignupRequest req) {

        User existing = userRepository.findByPhone(req.getPhone());
        if (existing != null)
            throw new RuntimeException("User already exists");

        User user = new User();
        user.setName(req.getName());
        user.setPhone(req.getPhone());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(Role.ROLE_USER);

        User saved = userRepository.save(user);

        // Auto login after signup
        Authentication auth = new UsernamePasswordAuthenticationToken(saved.getPhone(), req.getPassword());
        String jwt = jwtProvider.generateToken(auth);

        return new ResponseEntity<>(
                AuthResponse.builder()
                        .jwt("Bearer " + jwt)
                        .role(saved.getRole())
                        .message("User registered successfully")
                        .build(),
                HttpStatus.CREATED
        );
    }

    // -----------------------------------
    //             SIGN-IN
    // -----------------------------------

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {

        Authentication authentication = authenticate(req.getPhone(), req.getPassword());

        String jwt = jwtProvider.generateToken(authentication);

        // Extract role from authorities
        String roleName = authentication.getAuthorities()
                .iterator().next().getAuthority();  // Example: ROLE_USER

        Role role = Role.valueOf(roleName);

        return new ResponseEntity<>(
                AuthResponse.builder()
                        .jwt("Bearer " + jwt)
                        .role(role)
                        .message("Login successful")
                        .build(),
                HttpStatus.OK
        );
    }

    // -----------------------------------
    //             INTERNAL AUTH
    // -----------------------------------

    private Authentication authenticate(String phone, String password) {

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(phone);

        if (userDetails == null)
            throw new BadCredentialsException("Invalid phone or password");

        if (!passwordEncoder.matches(password, userDetails.getPassword()))
            throw new BadCredentialsException("Invalid phone or password");

        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                null,
                userDetails.getAuthorities()
        );
    }


    // ------------------ OTP: send code -------------------
    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, Object>> sendOtp(@RequestBody Map<String,String> payload) {
        String phone = payload.get("phone");
        if (phone == null || phone.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error","phone required"));
        }

        // generate 6-digit code
        String code = String.format("%06d", new Random().nextInt(1_000_000));
        LocalDateTime now = LocalDateTime.now();

        OtpCode otp = OtpCode.builder()
                .phone(phone)
                .code(code)
                .createdAt(now)
                .expiresAt(now.plusMinutes(5))
                .build();

        otpCodeRepository.save(otp);

        // TODO: integrate SMS provider here to send 'code' to phone number
        // For dev: return code in response (remove in production)
        return ResponseEntity.ok(Map.of("message", "OTP sent (dev)", "otp", code));
    }



    // ------------------ OTP: verify & login/register -------------------
    @Transactional
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String,Object>> verifyOtp(@RequestBody Map<String,String> payload) {
        String phone = payload.get("phone");
        String code = payload.get("code");

        if (phone == null || code == null) {
            return ResponseEntity.badRequest().body(Map.of("error","phone & code required"));
        }

        Optional<OtpCode> opt = otpCodeRepository.findTopByPhoneOrderByCreatedAtDesc(phone);
        if (opt.isEmpty()) return ResponseEntity.status(401).body(Map.of("error","Invalid OTP"));

        OtpCode otp = opt.get();
        if (otp.getExpiresAt().isBefore(LocalDateTime.now()) || !otp.getCode().equals(code)) {
            return ResponseEntity.status(401).body(Map.of("error","Invalid or expired OTP"));
        }

        // OTP valid — delete older codes
        otpCodeRepository.deleteByPhone(phone);

        // find or create user
        User user = userRepository.findByPhone(phone);
        if (user == null) {
            user = new User();
            user.setPhone(phone);
            user.setName("Customer"); // or accept name from payload
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString())); // random password
            user.setRole(Role.ROLE_USER);
            userRepository.save(user);
        }

        // create Authentication object
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getPhone(), null, List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority(user.getRole().name())));
        String jwt = jwtProvider.generateToken(auth);

        // create refresh token
        String refresh = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .token(refresh)
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiryDate(LocalDateTime.now().plusDays(30))
                .build();
        refreshTokenRepository.save(refreshToken);

        Map<String,Object> response = new HashMap<>();
        response.put("jwt", "Bearer " + jwt);
        response.put("refreshToken", refresh);
        response.put("role", user.getRole());
        return ResponseEntity.ok(response);
    }



    // ------------------ Refresh token -------------------
    @PostMapping("/refresh")
    public ResponseEntity<Map<String,Object>> refreshToken(@RequestBody Map<String,String> payload) {
        String refreshToken = payload.get("refreshToken");
        if (refreshToken == null || refreshToken.isBlank())
            return ResponseEntity.badRequest().body(Map.of("error","refreshToken required"));

        Optional<RefreshToken> rtOpt = refreshTokenRepository.findByToken(refreshToken);
        if (rtOpt.isEmpty()) return ResponseEntity.status(401).body(Map.of("error","Invalid refresh token"));

        RefreshToken rt = rtOpt.get();
        if (rt.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(rt);
            return ResponseEntity.status(401).body(Map.of("error","Refresh token expired"));
        }

        User user = rt.getUser();
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getPhone(), null, List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority(user.getRole().name())));
        String jwt = jwtProvider.generateToken(auth);

        // optionally rotate refresh token (recommended)
        String newRefresh = UUID.randomUUID().toString();
        rt.setToken(newRefresh);
        rt.setExpiryDate(LocalDateTime.now().plusDays(30));
        refreshTokenRepository.save(rt);

        Map<String,Object> res = new HashMap<>();
        res.put("jwt", "Bearer " + jwt);
        res.put("refreshToken", newRefresh);
        return ResponseEntity.ok(res);
    }
}




















































































































































































































































