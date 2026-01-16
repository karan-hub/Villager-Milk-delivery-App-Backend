package com.karan.village_milk_app.Response;

import com.karan.village_milk_app.model.Type.PaymentStatus;

public record PaymentStatusResponse(
        String amount,
        String orderType,
        PaymentStatus status
) {
}
