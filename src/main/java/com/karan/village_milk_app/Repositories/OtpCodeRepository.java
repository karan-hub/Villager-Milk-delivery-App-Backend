package com.karan.village_milk_app.Repositories;

import com.karan.village_milk_app.model.OtpCode;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;
import java.util.UUID;

public interface OtpCodeRepository extends JpaRepository<OtpCode, UUID> {
    Optional<OtpCode> findTopByPhoneOrderByCreatedAtDesc(String phone);
    @Modifying
    @Transactional
    void deleteByPhone(String phone);

}