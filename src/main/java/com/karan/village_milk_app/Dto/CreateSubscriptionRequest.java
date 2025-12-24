package com.karan.village_milk_app.Dto;


import com.karan.village_milk_app.model.Type.DeliverySlot;
import com.karan.village_milk_app.model.Type.PlanType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateSubscriptionRequest {

    @NotNull
    private UUID planId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private DeliverySlot deliverySlot; // MORNING / EVENING

    @NotNull
    @Positive
    private Integer quantity; // milk units per day

    @NotNull
    private PlanType planType; // DAILY / WEEKLY / MONTHLY
}
