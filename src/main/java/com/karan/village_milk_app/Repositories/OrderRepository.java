package com.karan.village_milk_app.Repositories;

import com.karan.village_milk_app.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Orders, UUID> {
    List<Orders> findByUserIdOrderByCreatedAtDesc(UUID userId);
    @Query("SELECT o FROM Orders o LEFT JOIN FETCH o.orderItems WHERE o.user.id = :userId ORDER BY o.createdAt DESC")
    List<Orders> findByUserIdWithItemsOrderByCreatedAtDesc(UUID userId);
    boolean existsBySubscriptionEventId(UUID eventId);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Orders o WHERE o.createdAt BETWEEN :start AND :end")
    BigDecimal sumTotalAmountByCreatedAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}