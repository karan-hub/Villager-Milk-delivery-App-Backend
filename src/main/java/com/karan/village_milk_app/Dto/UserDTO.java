package com.karan.village_milk_app.Dto;


import lombok.Data;

@Data
public class UserDTO {

    private Long id;

    private String name;
    private String phone;

    private String role;

    private AddressDTO address;
}
