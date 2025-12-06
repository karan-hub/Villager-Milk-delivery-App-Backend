package com.karan.village_milk_app.model.Type;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    CREATED("Created"),
    PENDING("Pending"),
    SUCCESS("Success"),
    FAILED("Failed"),
    REFUNDED("Refunded");

    private final String label;

    PaymentStatus(String label) {
        this.label = label;
    }

}