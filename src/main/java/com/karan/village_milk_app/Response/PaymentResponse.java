package com.karan.village_milk_app.Response;

import com.karan.village_milk_app.model.Type.OrderType;
import com.karan.village_milk_app.model.Type.PaymentMethod;
import com.karan.village_milk_app.model.Type.PaymentStatus;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

public record PaymentResponse(
        UUID paymentId,
        String paymentSessionId,
        String gatewayOrderId
) {}
