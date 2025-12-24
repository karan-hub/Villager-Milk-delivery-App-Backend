package com.karan.village_milk_app.Request;

import com.karan.village_milk_app.model.Type.DeliverySlot;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateOrderRequest {
    private List<CreateOrderItemRequest> items;
    private DeliverySlot deliverySlot;
    private LocalDateTime deliveryDate;

    public List<CreateOrderItemRequest> getItems() {
        return items;
    }

    public DeliverySlot getDeliverySlot() {
        return deliverySlot;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }
}
