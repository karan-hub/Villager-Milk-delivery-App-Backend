package com.karan.village_milk_app.model;

import com.karan.village_milk_app.model.Type.DeliverySlot;
import com.karan.village_milk_app.model.Type.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "subscriptions")
public class Subscriptions {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "subscription_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", columnDefinition = "BINARY(16)")
    private SubscriptionPlan plan;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;


}


