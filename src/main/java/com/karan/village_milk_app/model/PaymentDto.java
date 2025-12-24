package com.karan.village_milk_app.model;

import com.karan.village_milk_app.model.Type.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentDto {
    private UUID orderId;
    private BigDecimal amount;
    private PaymentStatus status; // PENDING / PAID / FAILED
    private String provider;      // Razorpay / Stripe
}
