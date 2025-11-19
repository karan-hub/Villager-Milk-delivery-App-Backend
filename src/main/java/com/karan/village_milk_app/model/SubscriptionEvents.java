package com.karan.village_milk_app.model;

import com.karan.village_milk_app.model.Type.DeliverySlot;
import com.karan.village_milk_app.model.Type.EventStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SubscriptionEvents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscriptions subscription;


    private LocalDate deliveryDate;

    @Enumerated(EnumType.STRING)
    private DeliverySlot deliverySlot;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
