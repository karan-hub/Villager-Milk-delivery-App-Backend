package com.karan.village_milk_app.Dto;



import lombok.Data;

@Data
public class AddressDTO {

    private Long id;

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
