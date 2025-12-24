package com.karan.village_milk_app.Dto;



import lombok.Data;

import java.util.UUID;

@Data
public class AddressDTO {

    private UUID id;
    private String flatNumber;
    private String buildingName;
    private String area;
    private String landmark;
    private String city;
    private String state;
    private String pincode;
    private Double latitude;
    private Double longitude;

    public UUID getId() {
        return id;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getArea() {
        return area;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPincode() {
        return pincode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
