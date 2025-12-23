package com.karan.village_milk_app.Service;

import java.time.LocalDate;
import java.util.Map;

public interface AdminAnalyticsService {

    /**
     * Get user statistics
     */
    Map<String, Object> getUserStatistics();

    /**
     * Get subscription statistics
     */
    Map<String, Object> getSubscriptionStatistics();

    /**
     * Get delivery statistics for a specific date range
     */
    Map<String, Object> getDeliveryStatistics(LocalDate startDate, LocalDate endDate);

    /**
     * Get revenue statistics for a specific date range
     */
    Map<String, Object> getRevenueStatistics(LocalDate startDate, LocalDate endDate);

    /**
     * Get overall dashboard statistics
     */
    Map<String, Object> getDashboardStatistics();
}