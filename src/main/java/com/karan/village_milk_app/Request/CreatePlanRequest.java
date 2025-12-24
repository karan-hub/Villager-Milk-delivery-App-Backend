package com.karan.village_milk_app.Request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreatePlanRequest {
    private String planKey;
    private String title;
    private Integer durationDays;
    private Integer units;
    private BigDecimal price;
    private String offer;
    private UUID productId;
}
