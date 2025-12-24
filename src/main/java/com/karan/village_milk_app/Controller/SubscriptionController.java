package com.karan.village_milk_app.Controller;

import com.karan.village_milk_app.Dto.CreateSubscriptionRequest;
import com.karan.village_milk_app.Response.SubscriptionDto;
import com.karan.village_milk_app.Service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionDto> create(
            @RequestBody CreateSubscriptionRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subscriptionService.createSubscription(req));
    }

    @GetMapping("/my")
    public ResponseEntity<List<SubscriptionDto>> mySubscriptions() {
        return ResponseEntity.ok(subscriptionService.getMySubscriptions());
    }

    @PutMapping("/events/{eventId}/skip")
    public ResponseEntity<Void> skip(@PathVariable UUID eventId) {
        subscriptionService.skipDelivery(eventId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/pause")
    public ResponseEntity<Void> pause(@PathVariable UUID id) {
        subscriptionService.pauseSubscription(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/resume")
    public ResponseEntity<Void> resume(@PathVariable UUID id) {
        subscriptionService.resumeSubscription(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable UUID id) {
        subscriptionService.cancelSubscription(id);
        return ResponseEntity.ok().build();
    }
}



