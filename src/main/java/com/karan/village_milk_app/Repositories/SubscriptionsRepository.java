package com.karan.village_milk_app.Repositories;

import com.karan.village_milk_app.model.Subscriptions;
import com.karan.village_milk_app.model.Type.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SubscriptionsRepository extends JpaRepository<Subscriptions, UUID> {
    List<Subscriptions> findByUserId(UUID userId);
    List<Subscriptions> findAllByStatus(SubscriptionStatus status);

    long countByStatus(SubscriptionStatus status);
    long countByCreatedAtAfter(LocalDateTime dateTime);
}