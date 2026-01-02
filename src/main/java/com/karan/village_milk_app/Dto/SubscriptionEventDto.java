package com.karan.village_milk_app.Dto;

import com.karan.village_milk_app.model.Type.DeliverySlot;
import com.karan.village_milk_app.model.Type.EventStatus;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class SubscriptionEventDto {
    private UUID eventId;
    private Instant deliveryDate;
    private DeliverySlot deliverySlot;
    private Integer quantity;
    private EventStatus status;

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
