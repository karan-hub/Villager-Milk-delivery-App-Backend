package com.karan.village_milk_app.Dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Getter
@Setter
@Data
public class ProductDto {

    private UUID id;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Product type is required")
    private String type; // cow / buffalo

    @NotBlank(message = "Unit is required")
    private String unit; // litre, ml

    @NotNull
    @Positive(message = "Price must be greater than zero")
    private BigDecimal price;

    @NotNull
    private Boolean inStock;

    @NotBlank
    private String imageUrl;

    private List<String> tags;
    private List<String> highlights;
    private List<String> benefits;

    private Map<String, String> nutrition;
}
