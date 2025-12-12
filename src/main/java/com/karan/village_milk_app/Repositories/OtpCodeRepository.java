package com.karan.village_milk_app.Repositories;

import com.karan.village_milk_app.model.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OtpCodeRepository extends JpaRepository<OtpCode, UUID> {
    Optional<OtpCode> findTopByPhoneOrderByCreatedAtDesc(String phone);
    void deleteByPhone(String phone);
}