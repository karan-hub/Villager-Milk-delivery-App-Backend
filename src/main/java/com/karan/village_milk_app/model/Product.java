package com.karan.village_milk_app.model;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Boolean inStock;

    private String imageUrl;


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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Subscriptions> subscription;

}

