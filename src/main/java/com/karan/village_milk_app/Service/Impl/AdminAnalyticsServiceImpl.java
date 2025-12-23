package com.karan.village_milk_app.Service.Impl;

import com.karan.village_milk_app.Repositories.*;
import com.karan.village_milk_app.Service.AdminAnalyticsService;
import com.karan.village_milk_app.model.Type.EventStatus;
import com.karan.village_milk_app.model.Type.SubscriptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @Override
    public Map<String, Object> getUserStatistics() {
        Map<String, Object> stats = new HashMap<>();

        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByRole("ROLE_USER");
        long adminUsers = userRepository.countByRole("ROLE_ADMIN");

        // Users registered in last 30 days
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        long newUsersLast30Days = userRepository.countByCreatedAtAfter(thirtyDaysAgo);

        stats.put("totalUsers", totalUsers);
        stats.put("activeUsers", activeUsers);
        stats.put("adminUsers", adminUsers);
        stats.put("newUsersLast30Days", newUsersLast30Days);

        return stats;
    }

    @Override
    public Map<String, Object> getSubscriptionStatistics() {
        Map<String, Object> stats = new HashMap<>();

        long totalSubscriptions = subscriptionsRepository.count();
        long activeSubscriptions = subscriptionsRepository.countByStatus(SubscriptionStatus.ACTIVE);
        long pausedSubscriptions = subscriptionsRepository.countByStatus(SubscriptionStatus.PAUSED);
        long cancelledSubscriptions = subscriptionsRepository.countByStatus(SubscriptionStatus.CANCELLED);

        // New subscriptions in last 30 days
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        long newSubscriptionsLast30Days = subscriptionsRepository.countByCreatedAtAfter(thirtyDaysAgo);

        stats.put("totalSubscriptions", totalSubscriptions);
        stats.put("activeSubscriptions", activeSubscriptions);
        stats.put("pausedSubscriptions", pausedSubscriptions);
        stats.put("cancelledSubscriptions", cancelledSubscriptions);
        stats.put("newSubscriptionsLast30Days", newSubscriptionsLast30Days);

        return stats;
    }

    @Override
    public Map<String, Object> getDeliveryStatistics(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> stats = new HashMap<>();

        if (startDate == null) startDate = LocalDate.now().minusDays(30);
        if (endDate == null) endDate = LocalDate.now();

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        long totalDeliveries = eventsRepository.countByDeliveryDateBetween(startDate, endDate);
        long delivered = eventsRepository.countByDeliveryDateBetweenAndStatus(startDate, endDate, EventStatus.DELIVERED);
        long missed = eventsRepository.countByDeliveryDateBetweenAndStatus(startDate, endDate, EventStatus.MISSED);
        long skipped = eventsRepository.countByDeliveryDateBetweenAndStatus(startDate, endDate, EventStatus.SKIPPED);

        double deliveryRate = totalDeliveries > 0 ? (double) delivered / totalDeliveries * 100 : 0;

        stats.put("totalDeliveries", totalDeliveries);
        stats.put("delivered", delivered);
        stats.put("missed", missed);
        stats.put("skipped", skipped);
        stats.put("deliveryRate", Math.round(deliveryRate * 100.0) / 100.0);
        stats.put("period", Map.of("startDate", startDate, "endDate", endDate));

        return stats;
    }

    @Override
    public Map<String, Object> getRevenueStatistics(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> stats = new HashMap<>();

        if (startDate == null) startDate = LocalDate.now().minusDays(30);
        if (endDate == null) endDate = LocalDate.now();

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        // Revenue from orders
        BigDecimal orderRevenue = orderRepository.sumTotalAmountByCreatedAtBetween(start, end);
        if (orderRevenue == null) orderRevenue = BigDecimal.ZERO;

        BigDecimal totalRevenue = orderRevenue;

        // Order count
        long orderCount = orderRepository.countByCreatedAtBetween(start, end);

        stats.put("totalRevenue", totalRevenue);
        stats.put("orderRevenue", orderRevenue);
        stats.put("orderCount", orderCount);
        stats.put("period", Map.of("startDate", startDate, "endDate", endDate));

        return stats;
    }

    @Override
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> dashboard = new HashMap<>();

        // Combine all statistics
        dashboard.put("userStats", getUserStatistics());
        dashboard.put("subscriptionStats", getSubscriptionStatistics());
        dashboard.put("deliveryStats", getDeliveryStatistics(null, null));
        dashboard.put("revenueStats", getRevenueStatistics(null, null));

        // Additional quick stats
        LocalDate today = LocalDate.now();
        long todaysDeliveries = eventsRepository.countByDeliveryDate(today);
        long todaysCompletedDeliveries = eventsRepository.countByDeliveryDateAndStatus(today, EventStatus.DELIVERED);

        dashboard.put("todaysDeliveries", todaysDeliveries);
        dashboard.put("todaysCompletedDeliveries", todaysCompletedDeliveries);
        dashboard.put("todaysCompletionRate", todaysDeliveries > 0 ?
            Math.round((double) todaysCompletedDeliveries / todaysDeliveries * 100 * 100.0) / 100.0 : 0);

        return dashboard;
    }
}