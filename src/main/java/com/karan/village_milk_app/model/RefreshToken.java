package com.karan.village_milk_app.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    private boolean revoked = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private Instant expiryDate;
    private Instant createdAt;
}
