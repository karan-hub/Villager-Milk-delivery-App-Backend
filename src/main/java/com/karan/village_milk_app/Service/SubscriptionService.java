package com.karan.village_milk_app.Service;

import com.karan.village_milk_app.Dto.CreateSubscriptionRequest;
import com.karan.village_milk_app.Request.CreateCustomSubscriptionRequest;
import com.karan.village_milk_app.Response.SubscriptionResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface SubscriptionService {
    SubscriptionResponse createSubscription(CreateSubscriptionRequest req);
    SubscriptionResponse createCustomSubscription(
            CreateCustomSubscriptionRequest request,
            UUID userId
    );
    List<SubscriptionResponse> getMySubscriptions();
    void skipDelivery(UUID eventId);
    void cancelSubscription(UUID subscriptionId);

    void pauseSubscription(UUID subscriptionId);
    void resumeSubscription(UUID subscriptionId);


    @Transactional(readOnly = true)
    List<SubscriptionResponse> getAllSubscriptions();
}
