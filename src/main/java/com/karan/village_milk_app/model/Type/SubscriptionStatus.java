package com.karan.village_milk_app.model.Type;

import lombok.Getter;

@Getter
public enum SubscriptionStatus {
    ACTIVE("Active"),
    PAUSED("Paused"),
    CANCELLED("Cancelled"),
    EXPIRED("Expired");

    private final String label;

    SubscriptionStatus(String label) {
        this.label = label;
    }

}