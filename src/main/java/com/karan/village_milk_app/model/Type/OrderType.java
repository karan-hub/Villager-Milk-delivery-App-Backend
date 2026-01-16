package com.karan.village_milk_app.model.Type;

public enum OrderType {
    SUBSCRIPTION("SUBSCRIPTION"),
    BUY_ONCE("BUY_ONCE");

    private final String label;

    OrderType(String label) {
        this.label = label;
    }

}
