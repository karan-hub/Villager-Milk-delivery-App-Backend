package com.karan.village_milk_app.Controller;

import com.karan.village_milk_app.Response.SubscriptionDto;
import com.karan.village_milk_app.Service.AdminSubscriptionService;
import com.karan.village_milk_app.model.DeliveryDto;
import com.karan.village_milk_app.model.Type.EventStatus;
import com.karan.village_milk_app.model.Type.SubscriptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminSubscriptionController {

    private final AdminSubscriptionService adminSubscriptionService;


    /**

     * GET /api/v1/admin/subscriptions
     * GET /api/v1/admin/subscriptions?status=ACTIVE
     * GET /api/v1/admin/subscriptions?status=PAUSED
     * GET /api/v1/admin/subscriptions?status=CANCELLED
     */
    @GetMapping("/subscriptions")
    public ResponseEntity<List<SubscriptionDto>> getSubscriptions(
            @RequestParam(required = false)
            SubscriptionStatus status
    ) {
        return ResponseEntity.ok(
                adminSubscriptionService.getSubscriptions(status)
        );
    }

    /* =================================================
       DELIVERIES (FILTER BY DATE + STATUS)
       ================================================= */

    /**

     * GET /api/v1/admin/deliveries
     * GET /api/v1/admin/deliveries?status=SCHEDULED
     * GET /api/v1/admin/deliveries?date=2025-01-12
     * GET /api/v1/admin/deliveries?date=2025-01-12&status=DELIVERED
     */
    @GetMapping("/deliveries")
    public ResponseEntity<List<DeliveryDto>> getDeliveries(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,

            @RequestParam(required = false)
            EventStatus status
    ) {
        return ResponseEntity.ok(
                adminSubscriptionService.getDeliveries(date, status)
        );
    }
}
