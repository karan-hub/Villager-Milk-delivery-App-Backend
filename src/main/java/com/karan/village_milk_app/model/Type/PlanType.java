package com.karan.village_milk_app.model.Type;

public enum PlanType {
    MONTHLY("monthly"),
    WEEKLY("weely"),
    DAILY("daily"),
    CUSTUME("custume");

    private final String label;

    PlanType(String label) {
        this.label = label;
    }
}
