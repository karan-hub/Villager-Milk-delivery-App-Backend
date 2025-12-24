package com.karan.village_milk_app.Service.Impl;

import com.karan.village_milk_app.Repositories.*;
import com.karan.village_milk_app.Service.AdminAnalyticsService;
import com.karan.village_milk_app.model.Type.EventStatus;
import com.karan.village_milk_app.model.Type.Role;
import com.karan.village_milk_app.model.Type.SubscriptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminAnalyticsServiceImpl implements AdminAnalyticsService {

    private final UserRepository userRepository;
    private final SubscriptionsRepository subscriptionsRepository;
    private final SubscriptionEventsRepository eventsRepository;
    private final OrderRepository orderRepository;

    /* =========================================================
       USER STATISTICS
       ========================================================= */
    @Override
    public Map<String, Object> getUserStatistics() {

        Map<String, Object> stats = new HashMap<>();

        LocalDateTime thirtyDaysAgo =
                LocalDateTime.now().minusDays(30);

        stats.put("totalUsers", userRepository.count());
        stats.put("activeUsers", userRepository.countByRole(Role.ROLE_USER));
        stats.put("adminUsers", userRepository.countByRole(Role.ROLE_ADMIN));
        stats.put(
                "newUsersLast30Days",
                userRepository.countByCreatedAtAfter(thirtyDaysAgo)
        );

        return stats;
    }

    /* =========================================================
       SUBSCRIPTION STATISTICS
       ========================================================= */
    @Override
    public Map<String, Object> getSubscriptionStatistics() {

        Map<String, Object> stats = new HashMap<>();

        LocalDateTime thirtyDaysAgo =
                LocalDateTime.now().minusDays(30);

        stats.put("totalSubscriptions", subscriptionsRepository.count());
        stats.put(
                "activeSubscriptions",
                subscriptionsRepository.countByStatus(SubscriptionStatus.ACTIVE)
        );
        stats.put(
                "pausedSubscriptions",
                subscriptionsRepository.countByStatus(SubscriptionStatus.PAUSED)
        );
        stats.put(
                "cancelledSubscriptions",
                subscriptionsRepository.countByStatus(SubscriptionStatus.CANCELLED)
        );
        stats.put(
                "newSubscriptionsLast30Days",
                subscriptionsRepository.countByCreatedAtAfter(thirtyDaysAgo)
        );

        return stats;
    }

    /* =========================================================
       DELIVERY STATISTICS
       ========================================================= */
    @Override
    public Map<String, Object> getDeliveryStatistics(
            LocalDate startDate,
            LocalDate endDate
    ) {

        Map<String, Object> stats = new HashMap<>();

        LocalDate start =
                (startDate != null) ? startDate : LocalDate.now().minusDays(30);
        LocalDate end =
                (endDate != null) ? endDate : LocalDate.now();

        long total =
                eventsRepository.countByDeliveryDateBetween(start, end);

        long delivered =
                eventsRepository.countByDeliveryDateBetweenAndStatus(
                        start, end, EventStatus.DELIVERED
                );

        long missed =
                eventsRepository.countByDeliveryDateBetweenAndStatus(
                        start, end, EventStatus.MISSED
                );

        long skipped =
                eventsRepository.countByDeliveryDateBetweenAndStatus(
                        start, end, EventStatus.SKIPPED
                );

        double deliveryRate =
                total > 0 ? (double) delivered * 100 / total : 0;

        stats.put("totalDeliveries", total);
        stats.put("delivered", delivered);
        stats.put("missed", missed);
        stats.put("skipped", skipped);
        stats.put("deliveryRate", Math.round(deliveryRate * 100.0) / 100.0);
        stats.put("period", Map.of(
                "startDate", start,
                "endDate", end
        ));

        return stats;
    }

    /* =========================================================
       REVENUE STATISTICS
       ========================================================= */
    @Override
    public Map<String, Object> getRevenueStatistics(
            LocalDate startDate,
            LocalDate endDate
    ) {

        Map<String, Object> stats = new HashMap<>();

        LocalDate start =
                (startDate != null) ? startDate : LocalDate.now().minusDays(30);
        LocalDate end =
                (endDate != null) ? endDate : LocalDate.now();

        LocalDateTime startDateTime =
                start.atStartOfDay();

        LocalDateTime endDateTime =
                end.atTime(LocalTime.MAX);

        BigDecimal orderRevenue =
                orderRepository.sumTotalAmountByCreatedAtBetween(
                        startDateTime, endDateTime
                );

        if (orderRevenue == null) {
            orderRevenue = BigDecimal.ZERO;
        }

        long orderCount =
                orderRepository.countByCreatedAtBetween(
                        startDateTime, endDateTime
                );

        stats.put("totalRevenue", orderRevenue);
        stats.put("orderRevenue", orderRevenue);
        stats.put("orderCount", orderCount);
        stats.put("period", Map.of(
                "startDate", start,
                "endDate", end
        ));

        return stats;
    }

    /* =========================================================
       DASHBOARD STATISTICS
       ========================================================= */
    @Override
    public Map<String, Object> getDashboardStatistics() {

        Map<String, Object> dashboard = new HashMap<>();

        dashboard.put("userStats", getUserStatistics());
        dashboard.put("subscriptionStats", getSubscriptionStatistics());
        dashboard.put("deliveryStats", getDeliveryStatistics(null, null));
        dashboard.put("revenueStats", getRevenueStatistics(null, null));

        LocalDate today = LocalDate.now();

        long todaysDeliveries =
                eventsRepository.countByDeliveryDate(today);

        long todaysCompletedDeliveries =
                eventsRepository.countByDeliveryDateAndStatus(
                        today, EventStatus.DELIVERED
                );

        double completionRate =
                todaysDeliveries > 0
                        ? (double) todaysCompletedDeliveries * 100 / todaysDeliveries
                        : 0;

        dashboard.put("todaysDeliveries", todaysDeliveries);
        dashboard.put("todaysCompletedDeliveries", todaysCompletedDeliveries);
        dashboard.put(
                "todaysCompletionRate",
                Math.round(completionRate * 100.0) / 100.0
        );

        return dashboard;
    }
}