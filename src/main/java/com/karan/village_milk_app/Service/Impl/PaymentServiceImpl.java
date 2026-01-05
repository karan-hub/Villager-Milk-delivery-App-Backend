package com.karan.village_milk_app.Service.Impl;

import com.karan.village_milk_app.Repositories.PaymentsRepository;
import com.karan.village_milk_app.Repositories.SubscriptionsRepository;
import com.karan.village_milk_app.Request.InitiatePaymentRequest;
import com.karan.village_milk_app.Response.PaymentResponse;
import com.karan.village_milk_app.Service.PaymentService;
import com.karan.village_milk_app.model.Payments;
import com.karan.village_milk_app.model.Subscriptions;
import com.karan.village_milk_app.model.Type.PaymentStatus;
import com.karan.village_milk_app.model.Type.SubscriptionStatus;
import com.karan.village_milk_app.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentsRepository paymentRepository;
    private final SubscriptionsRepository subscriptionRepository;

    @Override
    public PaymentResponse initiatePayment(
            User user,
            InitiatePaymentRequest request
    ) {
        Subscriptions subscription = subscriptionRepository
                .findById(request.subscriptionId())
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        Payments payment = new Payments();
        payment.setUser(user);
        payment.setSubscription(subscription);
        payment.setAmount(request.amount());
        payment.setPaymentMethod(request.paymentMethod());
        payment.setPaymentStatus(PaymentStatus.PENDING);

        Payments saved = paymentRepository.save(payment);

        return new PaymentResponse(
                saved.getId(),
                saved.getPaymentStatus()
        );
    }

    @Override
    public void confirmPayment(UUID paymentId) {
        Payments payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setPaymentStatus(PaymentStatus.SUCCESS);

        Subscriptions subscription = payment.getSubscription();
        if (subscription != null) {
            subscription.setStatus(SubscriptionStatus.ACTIVE);
        }
    }
}
