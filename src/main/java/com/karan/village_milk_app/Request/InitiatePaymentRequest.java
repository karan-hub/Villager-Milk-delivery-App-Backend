package com.karan.village_milk_app.Request;

import com.karan.village_milk_app.model.Type.OrderType;
import com.karan.village_milk_app.model.Type.PaymentMethod;

import java.math.BigDecimal;
import java.util.UUID;

public record InitiatePaymentRequest(
        OrderType orderType,
        UUID orderTypeId,
        BigDecimal amount,
        PaymentMethod paymentMethod
) {}
