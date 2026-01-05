package com.karan.village_milk_app.Request;

import java.util.UUID;

public record ConfirmPaymentRequest(
        UUID paymentId
) {}

