package com.karan.village_milk_app.Response;

import com.karan.village_milk_app.model.Type.DeliverySlot;
import com.karan.village_milk_app.model.Type.SubscriptionStatus;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class SubscriptionResponse  {
    private UUID subscriptionId;
    private String planTitle;
    private String productName;
    private Integer units;
    private LocalDate startDate;
    private LocalDate endDate;
    private DeliverySlot deliverySlot;
    private SubscriptionStatus status;
    private LocalDateTime createdAt;
    private UUID userId;
    private UUID planId;
    private List<DeliveryRuleResponse> deliveryRules;

}

