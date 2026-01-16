package com.karan.village_milk_app.model.Type;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("Pending"),
    PAID("Paid"),
    PROCESSING("Processing"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled"),
    CONFIRMED("CONFIRMED");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

}