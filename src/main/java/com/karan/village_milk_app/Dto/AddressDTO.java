package com.karan.village_milk_app.Dto;



import lombok.Data;

import java.util.UUID;

@Data
public class AddressDTO {
    private UUID  id;
    private String flatNumber;
    private String buildingName;
    private String area;
    private String landmark;
    private String city;
    private String state;
    private String pincode;
    private Double latitude;
    private Double longitude;
}
