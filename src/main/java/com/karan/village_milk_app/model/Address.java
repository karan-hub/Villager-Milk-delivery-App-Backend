package com.karan.village_milk_app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address    {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "address_id" ,columnDefinition = "BINARY(16)")
    private UUID id;

    private String flatNumber;
    private String buildingName;
    private String area;
    private String landmark;
    private String city;
    private String state;
    private String pincode;
    private String country = "India";

    private Double latitude;
    private Double longitude;

    private boolean isDefault = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" ,columnDefinition = "BINARY(16)")
    @JsonIgnore
    private User user;

}

