package com.karan.village_milk_app.Request;

import com.karan.village_milk_app.model.Type.DeliverySlot;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateSubscriptionRequest {

    private UUID userId;
    private UUID planId;
    private LocalDate startDate;
    private DeliverySlot deliverySlot;
}
