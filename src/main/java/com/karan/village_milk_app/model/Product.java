package com.karan.village_milk_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name; // Cow Milk, Desi Cow Ghee
    private String type; // cow, buffalo, ghee

    private String unit;  // "500ml", "1L", "500g", "1kg"
    private BigDecimal price;

    private Boolean inStock = true;

    private String imageUrl; // store single URL



    @ElementCollection
    private List<String> tags;

    @ElementCollection
    private List<String> highlights;

    @ElementCollection
    private List<String> benefits;

    @ElementCollection
    @CollectionTable(
            name = "product_nutrition",
            joinColumns = @JoinColumn(name = "product_id")
    )
    @MapKeyColumn(name = "nutrient")
    @Column(name = "value")
    private Map<String, String> nutrition;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<SubscriptionPlan> subscriptionPlans;
}

