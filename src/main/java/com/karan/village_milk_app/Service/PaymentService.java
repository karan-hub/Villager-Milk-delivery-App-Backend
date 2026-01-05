package com.karan.village_milk_app.Service;

import com.karan.village_milk_app.Request.InitiatePaymentRequest;
import com.karan.village_milk_app.Response.PaymentResponse;
import com.karan.village_milk_app.model.User;

import java.util.UUID;

public interface PaymentService {
    public void confirmPayment(UUID paymentId);
    public PaymentResponse initiatePayment(
            User user,
            InitiatePaymentRequest request
    );
}
