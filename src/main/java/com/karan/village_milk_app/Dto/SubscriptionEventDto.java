package com.karan.village_milk_app.Dto;

import com.karan.village_milk_app.model.Type.DeliverySlot;
import com.karan.village_milk_app.model.Type.EventStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class SubscriptionEventDto {
    private UUID eventId;
    private LocalDate deliveryDate;
    private DeliverySlot deliverySlot;
    private Integer quantity;
    private EventStatus status; // PENDING / SKIPPED / DELIVERED

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
