package com.karan.village_milk_app.Repositories;

import com.karan.village_milk_app.Response.SubscriptionResponse;
import com.karan.village_milk_app.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionPlanRepository
        extends JpaRepository<SubscriptionPlan, UUID> {
    List<SubscriptionPlan> findAllByProduct_Id(UUID productId);

}
