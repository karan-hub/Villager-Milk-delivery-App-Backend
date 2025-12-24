package com.karan.village_milk_app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String planKey; // weekly, monthly, fortnight
    private String title;
    private Integer durationDays;
    private Integer units;
    private BigDecimal price;
    private String offer;
    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", columnDefinition = "BINARY(16)")
    @JsonIgnore
    private Product product;


}
