package com.karan.village_milk_app.Response;

import java.math.BigDecimal;

public record CashfreeVerifyResponse(
        String orderStatus,
        BigDecimal amount,
        String cfPaymentId
) {}
