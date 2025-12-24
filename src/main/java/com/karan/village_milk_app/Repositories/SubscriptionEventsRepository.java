package com.karan.village_milk_app.Repositories;

import com.karan.village_milk_app.model.SubscriptionEvents;
import com.karan.village_milk_app.model.Type.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SubscriptionEventsRepository extends JpaRepository<SubscriptionEvents, UUID> {
    boolean existsByIdAndStatus(UUID id, EventStatus status);
    List<SubscriptionEvents> findByDeliveryDateAndStatus(
            LocalDate date,
            EventStatus status
    );
    List<SubscriptionEvents> findBySubscriptionIdAndDeliveryDateAfter(
            UUID subscriptionId,
            LocalDate date
    );

    List<SubscriptionEvents> findByDeliveryDate(LocalDate targetDate);

    long countByDeliveryDate(LocalDate date);
    long countByDeliveryDateAndStatus(LocalDate date, EventStatus status);
    long countByDeliveryDateBetween(LocalDate startDate, LocalDate endDate);
    long countByDeliveryDateBetweenAndStatus(LocalDate startDate, LocalDate endDate, EventStatus status);
}