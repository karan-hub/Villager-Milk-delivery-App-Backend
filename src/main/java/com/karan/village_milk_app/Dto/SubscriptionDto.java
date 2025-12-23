package com.karan.village_milk_app.Dto;

import com.karan.village_milk_app.model.Type.SubscriptionStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class SubscriptionDto {

    private UUID subscriptionId;
    private String planTitle;
    private LocalDate startDate;
    private LocalDate endDate;
    private SubscriptionStatus status;
}
