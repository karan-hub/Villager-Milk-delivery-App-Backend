package com.karan.village_milk_app.Dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class SubscriptionPlanDto {
    private UUID id;
    private String planKey;        // weekly | fortnight | monthly
    private String title;          // Monthly Cow Milk Plan
    private Integer durationDays;  // 7 / 15 / 30
    private Integer units;         // litres per day
    private BigDecimal price;      // price per unit/day
    private String offer;          // Save ₹150
    private UUID productId;
    private String productName;    // for UI

    public void setId(UUID id) {
        this.id = id;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
