package com.karan.village_milk_app.Request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateOrderItemRequest {
    private UUID productId;
    private Long quantity;
}
