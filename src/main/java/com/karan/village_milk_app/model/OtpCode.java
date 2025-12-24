package com.karan.village_milk_app.model;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "otp_codes",
        indexes = {
                @Index(name = "idx_otp_phone", columnList = "phone")
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
public class OtpCode {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String otpHash;

    @Column(nullable = false, updatable = false)
    private Instant expiresAt;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;


}
