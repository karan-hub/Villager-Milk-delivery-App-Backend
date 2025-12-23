package com.karan.village_milk_app.Controller;

import com.karan.village_milk_app.Service.AdminAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/analytics")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminAnalyticsController {

    private final AdminAnalyticsService adminAnalyticsService;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStatistics() {
        return ResponseEntity.ok(adminAnalyticsService.getDashboardStatistics());
    }

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getUserStatistics() {
        return ResponseEntity.ok(adminAnalyticsService.getUserStatistics());
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<Map<String, Object>> getSubscriptionStatistics() {
        return ResponseEntity.ok(adminAnalyticsService.getSubscriptionStatistics());
    }

    @GetMapping("/deliveries")
    public ResponseEntity<Map<String, Object>> getDeliveryStatistics(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(adminAnalyticsService.getDeliveryStatistics(startDate, endDate));
    }

    @GetMapping("/revenue")
    public ResponseEntity<Map<String, Object>> getRevenueStatistics(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(adminAnalyticsService.getRevenueStatistics(startDate, endDate));
    }
}