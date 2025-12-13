package com.karan.village_milk_app.Service.Impl;

import com.karan.village_milk_app.Config.OtpProperties;
import com.karan.village_milk_app.Dto.UserDTO;
import com.karan.village_milk_app.Repositories.OtpCodeRepository;
import com.karan.village_milk_app.Repositories.UserRepository;
import com.karan.village_milk_app.Service.OtpSender;
import com.karan.village_milk_app.Service.OtpService;
import com.karan.village_milk_app.Service.UserService;
import com.karan.village_milk_app.healpers.OtpGenerator;
import com.karan.village_milk_app.model.OtpCode;
import com.karan.village_milk_app.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpCodeRepository otpRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpGenerator otpGenerator;
    private final OtpProperties otpProperties;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final Clock clock;
    private final OtpSender otpSender; // SMS / WhatsApp abstraction

    @Override
    public void requestOtp(String phone) {

        // Delete previous OTPs
        otpRepository.deleteByPhone(phone);

        String otp = otpGenerator.generate();

        OtpCode otpCode = new OtpCode();
        otpCode.setPhone(phone);
        otpCode.setOtpHash(passwordEncoder.encode(otp));
        otpCode.setCreatedAt(Instant.now(clock));
        otpCode.setExpiresAt(
                Instant.now(clock)
                        .plus(Duration.ofMinutes(otpProperties.getExpiryMinutes()))
        );

        otpRepository.save(otpCode);

        // Send OTP securely
        otpSender.send(phone, otp);
    }

    @Override
    public UserDTO verifyOtp(String phone, String otp) {

        OtpCode otpCode = otpRepository
                .findTopByPhoneOrderByCreatedAtDesc(phone)
                .orElseThrow(() -> new IllegalArgumentException("OTP not found"));

        if (Instant.now(clock).isAfter(otpCode.getExpiresAt())) {
            throw new IllegalArgumentException("OTP expired");
        }

        if (!passwordEncoder.matches(otp, otpCode.getOtpHash())) {
            throw new IllegalArgumentException("Invalid OTP");
        }

        otpRepository.deleteByPhone(phone);

        User user = userRepository.findByPhone(phone)
                .orElseGet(() ->
                        modelMapper.map(
                                userService.createUserViaOtp(phone),
                                User.class
                        )
                );

        return modelMapper.map(user, UserDTO.class);
    }


}

