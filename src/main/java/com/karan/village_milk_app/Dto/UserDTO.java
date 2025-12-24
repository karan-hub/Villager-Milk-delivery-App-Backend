package com.karan.village_milk_app.Dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.karan.village_milk_app.model.Type.Role;
import lombok.*;

import java.time.Instant;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO{
    private UUID id;
    private String name;
    private String phone;
    @JsonIgnore
    private String password;
    private Role role;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean enabled;
    private AddressDTO address;


}