package com.karan.village_milk_app.model.Type;

public enum PaymentMethod {
    UPI("UPI"),
    CASH("CASH");

    private final String label;

    PaymentMethod(String label) {
        this.label = label;
    }
}
