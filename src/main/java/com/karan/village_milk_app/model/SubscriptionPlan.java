package com.karan.village_milk_app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String planKey; // weekly, monthly, fortnight
    private String title;
    private Integer durationDays;
    private Integer units;
    private BigDecimal price;
    private String offer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Product product;
}
