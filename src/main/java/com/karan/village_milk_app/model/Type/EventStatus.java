package com.karan.village_milk_app.model.Type;

import lombok.Getter;

@Getter
public enum EventStatus {
    SCHEDULED("Scheduled"),
    SKIPPED("Skipped"),
    DELIVERED("Delivered"),
    MISSED("Missed"),
    CANCELLED("Cancelled");

    private final String label;

    EventStatus(String label) {
        this.label = label;
    }

}
