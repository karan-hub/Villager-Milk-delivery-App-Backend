package com.karan.village_milk_app.Service;

import com.karan.village_milk_app.Dto.CreateSubscriptionRequest;
import com.karan.village_milk_app.Response.SubscriptionDto;
import com.karan.village_milk_app.model.DeliveryDto;
import com.karan.village_milk_app.model.Subscriptions;
import com.karan.village_milk_app.model.Type.DeliverySlot;
import com.karan.village_milk_app.model.Type.EventStatus;
import com.karan.village_milk_app.model.Type.SubscriptionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SubscriptionService {
    SubscriptionDto createSubscription(CreateSubscriptionRequest req);
    List<SubscriptionDto> getMySubscriptions();
    void skipDelivery(UUID eventId);
    void cancelSubscription(UUID subscriptionId);

    void pauseSubscription(UUID subscriptionId);
    void resumeSubscription(UUID subscriptionId);


    @Transactional(readOnly = true)
    List<SubscriptionDto> getAllSubscriptions();
}
