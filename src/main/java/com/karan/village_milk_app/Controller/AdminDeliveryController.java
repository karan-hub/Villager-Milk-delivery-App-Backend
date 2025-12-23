package com.karan.village_milk_app.Controller;

import com.karan.village_milk_app.Dto.SubscriptionEventDto;
import com.karan.village_milk_app.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/deliveries")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminDeliveryController {

    private final AdminService adminService;


    @GetMapping("/today")
    public ResponseEntity<List<SubscriptionEventDto>> todayDeliveries() {
        return ResponseEntity.ok(adminService.getTodayDeliveries());
    }


    @PutMapping("/{eventId}/deliver")
    public ResponseEntity<Void> markDelivered(@PathVariable UUID eventId) {
        adminService.markDelivered(eventId);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{eventId}/missed")
    public ResponseEntity<Void> markMissed(@PathVariable UUID eventId) {
        adminService.markMissed(eventId);
        return ResponseEntity.ok().build();
    }
}
