package com.karan.village_milk_app.Response;

import com.karan.village_milk_app.model.Type.PaymentStatus;

import java.util.UUID;

public record PaymentResponse(
        UUID paymentId,
        PaymentStatus status
) {}
