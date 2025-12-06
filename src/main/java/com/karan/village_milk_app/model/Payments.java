package com.karan.village_milk_app.model;
import com.karan.village_milk_app.model.Type.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Subscriptions subscription;

    private BigDecimal amount;

    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "razorpay_payment_id")
    private String  PaymentId;
    @Column(name = "razorpay_order_id")
    private String  payOrderId;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}

