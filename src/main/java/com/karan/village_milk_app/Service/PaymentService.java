package com.karan.village_milk_app.Service;

import com.karan.village_milk_app.Request.InitiatePaymentRequest;
import com.karan.village_milk_app.Response.PaymentResponse;
import com.karan.village_milk_app.model.Type.PaymentStatus;
import com.karan.village_milk_app.model.User;

import java.util.UUID;

public interface PaymentService {
    PaymentResponse initiate(User user, InitiatePaymentRequest request);
    PaymentStatus verify(UUID paymentId);
}
