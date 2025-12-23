package com.karan.village_milk_app.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItemDto {
    private UUID productId;
    private String productName;
    private Long quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;


}

