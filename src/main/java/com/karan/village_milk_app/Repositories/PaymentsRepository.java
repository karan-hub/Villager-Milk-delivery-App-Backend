package com.karan.village_milk_app.Repositories;

import com.karan.village_milk_app.model.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PaymentsRepository extends JpaRepository<Payments, UUID> {
   Optional<Payments> findByMyOrderId(String OrderId);
    @Query("select p.myOrderId from Payments p where p.id = :id")
    String findGatewayOrderId(@Param("id") UUID paymentId);
}