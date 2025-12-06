package com.karan.village_milk_app.model.Type;

import lombok.Getter;

@Getter
public enum DeliverySlot {
    MORNING("Morning"),
    EVENING("Evening");

    private final String label;

    DeliverySlot(String label) {
        this.label = label;
    }

}