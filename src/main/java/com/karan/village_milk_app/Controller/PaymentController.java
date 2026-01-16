package com.karan.village_milk_app.Controller;

import com.karan.village_milk_app.Request.InitiatePaymentRequest;
import com.karan.village_milk_app.Response.PaymentResponse;
import com.karan.village_milk_app.Service.PaymentService;
import com.karan.village_milk_app.model.Type.PaymentStatus;
import com.karan.village_milk_app.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Controller
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse> initiate(
            @RequestBody InitiatePaymentRequest request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(
                paymentService.initiate(user, request)
        );
    }

    @PostMapping("/verify/{paymentId}")
    public ResponseEntity<Map<String, String>> verify(
            @PathVariable UUID paymentId
    ) {
        PaymentStatus status = paymentService.verify(paymentId);

        return ResponseEntity.ok(
                Map.of(
                        "paymentId", paymentId.toString(),
                        "status", status.name()
                )
        );
    }


}
