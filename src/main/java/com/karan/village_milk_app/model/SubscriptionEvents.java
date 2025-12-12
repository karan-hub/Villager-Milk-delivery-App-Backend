package com.karan.village_milk_app.model;

import com.karan.village_milk_app.model.Type.DeliverySlot;
import com.karan.village_milk_app.model.Type.EventStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SubscriptionEvents {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "event_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", columnDefinition = "BINARY(16)")
    private Subscriptions subscription;

    private LocalDate deliveryDate;

    @Enumerated(EnumType.STRING)
    private DeliverySlot deliverySlot;

    private Integer deliveredQuantity;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @CreatedDate
    private Instant createdAt;
}
