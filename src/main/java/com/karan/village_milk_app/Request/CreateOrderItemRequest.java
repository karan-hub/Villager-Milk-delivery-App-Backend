package com.karan.village_milk_app.Request;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateOrderItemRequest {
    private UUID productId;
    private Long quantity;

    public Long getQuantity() {
        return quantity;
    }

    public UUID getProductId() {
        return productId;
    }
}
