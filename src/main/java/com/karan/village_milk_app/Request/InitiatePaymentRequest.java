package com.karan.village_milk_app.Request;

import java.math.BigDecimal;
import java.util.UUID;

public record InitiatePaymentRequest(
        UUID subscriptionId,
        BigDecimal amount,
        String paymentMethod
) {}
