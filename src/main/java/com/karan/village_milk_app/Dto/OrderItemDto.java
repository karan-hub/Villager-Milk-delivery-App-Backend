package com.karan.village_milk_app.Dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
public class OrderItemDto {
    private String productName;
    private Long quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
}


