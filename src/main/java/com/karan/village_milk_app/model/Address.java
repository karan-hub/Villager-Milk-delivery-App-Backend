package com.karan.village_milk_app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "addresses",
        indexes = @Index(name = "idx_address_user", columnList = "user_id"))
public class Address    {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "address_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String flatNumber;

    private String buildingName;

    @Column(nullable = false)
    private String area;

    private String landmark;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false, length = 6)
    private String pincode;

    @Column(nullable = false)
    private String country = "India";

    private Double latitude;
    private Double longitude;

    @Column(nullable = false)
    private boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)")
    private User user;

}

