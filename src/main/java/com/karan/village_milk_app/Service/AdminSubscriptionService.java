package com.karan.village_milk_app.Service;

import com.karan.village_milk_app.Response.SubscriptionDto;
import com.karan.village_milk_app.model.DeliveryDto;
import com.karan.village_milk_app.model.Type.EventStatus;
import com.karan.village_milk_app.model.Type.SubscriptionStatus;

import java.time.LocalDate;
import java.util.List;

public interface AdminSubscriptionService {
    List<SubscriptionDto> getSubscriptions(SubscriptionStatus status);

    List<DeliveryDto> getDeliveries(LocalDate date, EventStatus status);
}
