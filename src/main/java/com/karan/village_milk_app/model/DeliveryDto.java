package com.karan.village_milk_app.model;

import com.karan.village_milk_app.model.Type.DeliverySlot;
import com.karan.village_milk_app.model.Type.EventStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class DeliveryDto {
    private UUID eventId;
    private UUID userId;
    private UUID subscriptionId;
    private String userName;
    private String productName;
    private Integer quantity;
    private DeliverySlot deliverySlot;
    private LocalDate deliveryDate;
    private EventStatus status;
}
