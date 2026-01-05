package com.karan.village_milk_app.Response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AddressResponse {
    private UUID id;

    private String flatNumber;
    private String buildingName;
    private String area;
    private String landmark;

    private String city;
    private String state;
    private String pincode;
    private String country;

    private Double latitude;
    private Double longitude;

    private boolean isDefault;
}
