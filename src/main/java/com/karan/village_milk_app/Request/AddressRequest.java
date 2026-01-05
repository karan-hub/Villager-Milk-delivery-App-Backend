package com.karan.village_milk_app.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.UUID;

@Data
public class AddressRequest {


    private UUID userId;

    @NotBlank(message = "Flat number is required")
    private String flatNumber;

    private String buildingName;

    @NotBlank(message = "Area is required")
    private String area;

    private String landmark;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @Pattern(regexp = "\\d{6}", message = "Pincode must be 6 digits")
    private String pincode;

    private Double latitude;
    private Double longitude;
}
