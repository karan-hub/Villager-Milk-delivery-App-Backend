package com.karan.village_milk_app.Repositories;

import com.karan.village_milk_app.model.Subscriptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.UUID;

public interface SubscriptionsRepository extends JpaRepository<Subscriptions, UUID> {
}