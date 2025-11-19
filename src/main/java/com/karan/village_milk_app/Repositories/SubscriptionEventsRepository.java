package com.karan.village_milk_app.Repositories;

import com.karan.village_milk_app.model.SubscriptionEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface SubscriptionEventsRepository extends JpaRepository<SubscriptionEvents, Long> {
}