package com.karan.village_milk_app.Repositories;

import com.karan.village_milk_app.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}