package com.karan.village_milk_app.model.Type;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_USER("User"),
    ROLE_ADMIN("Admin"),
    ROLE_DELIVERY("Delivery Person"),
    ROLE_STAFF("Staff");

    private final String label;

    Role(String label) {
        this.label = label;
    }

}
