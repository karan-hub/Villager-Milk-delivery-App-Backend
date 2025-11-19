package com.karan.village_milk_app.Repositories;

import com.karan.village_milk_app.model.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface PaymentsRepository extends JpaRepository<Payments, Long> {
}