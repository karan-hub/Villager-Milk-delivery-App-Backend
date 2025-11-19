package com.karan.village_milk_app.Dto;

import com.karan.village_milk_app.model.Address;

public class AddressMapper {

    public static AddressDTO toDTO(Address address) {
        if (address == null) return null;

        AddressDTO dto = new AddressDTO();

        dto.setId(address.getId());
        dto.setFlatNumber(address.getFlatNumber());
        dto.setBuildingName(address.getBuildingName());
        dto.setArea(address.getArea());
        dto.setLandmark(address.getLandmark());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setPincode(address.getPincode());
        dto.setLatitude(address.getLatitude());
        dto.setLongitude(address.getLongitude());

        return dto;
    }

    public static void updateEntity(Address address, AddressDTO dto) {
        if (dto.getFlatNumber() != null) address.setFlatNumber(dto.getFlatNumber());
        if (dto.getBuildingName() != null) address.setBuildingName(dto.getBuildingName());
        if (dto.getArea() != null) address.setArea(dto.getArea());
        if (dto.getLandmark() != null) address.setLandmark(dto.getLandmark());
        if (dto.getCity() != null) address.setCity(dto.getCity());
        if (dto.getState() != null) address.setState(dto.getState());
        if (dto.getPincode() != null) address.setPincode(dto.getPincode());
        if (dto.getLatitude() != null) address.setLatitude(dto.getLatitude());
        if (dto.getLongitude() != null) address.setLongitude(dto.getLongitude());
    }
}