package com.karan.village_milk_app.Controller;

import com.karan.village_milk_app.Request.ConfirmPaymentRequest;
import com.karan.village_milk_app.Request.InitiatePaymentRequest;
import com.karan.village_milk_app.Response.PaymentResponse;
import com.karan.village_milk_app.Service.Impl.PaymentServiceImpl;
import com.karan.village_milk_app.Service.PaymentService;
import com.karan.village_milk_app.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentServiceImpl paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse> initiate(
            @RequestBody InitiatePaymentRequest request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(
                paymentService.initiatePayment(user, request)
        );
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirm(
            @RequestBody ConfirmPaymentRequest request
    ) {
        paymentService.confirmPayment(request.paymentId());
        return ResponseEntity.ok().build();
    }
}
