package com.karan.village_milk_app.Repositories;

import com.karan.village_milk_app.model.SubscriptionDeliveryRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubscriptionDeliveryRuleRepo extends JpaRepository<SubscriptionDeliveryRule , UUID> {
}
