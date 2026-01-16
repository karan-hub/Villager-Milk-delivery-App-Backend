package com.karan.village_milk_app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.DayOfWeek;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "subscription_delivery_rules")
@Getter
@Setter
public class SubscriptionDeliveryRule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscriptions subscription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek; // MONDAY .. SUNDAY

    @Column(nullable = false)
    private Integer units;

    @CreatedDate
    private Instant createdAt= Instant.now();

}
