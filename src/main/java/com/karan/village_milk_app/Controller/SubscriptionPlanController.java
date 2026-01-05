package com.karan.village_milk_app.Controller;

import com.karan.village_milk_app.Dto.SubscriptionPlanDto;
import com.karan.village_milk_app.Repositories.ProductRepository;
import com.karan.village_milk_app.Repositories.SubscriptionPlanRepository;
import com.karan.village_milk_app.Request.CreatePlanRequest;
import com.karan.village_milk_app.Service.SubscriptionPlanService;
import com.karan.village_milk_app.model.Product;
import com.karan.village_milk_app.model.SubscriptionPlan;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subscription-plans")
@RequiredArgsConstructor
public class SubscriptionPlanController {

    private final SubscriptionPlanService planService;

    @GetMapping("/all")
    public ResponseEntity<List<SubscriptionPlanDto>> getAllPlans() {
        return ResponseEntity.ok(planService.getAllPlans());
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<List<SubscriptionPlanDto>> getProductPlan(
            @PathVariable UUID id) {

        return ResponseEntity.ok(planService.getPlansByProductId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubscriptionPlanDto> create(
            @RequestBody SubscriptionPlanDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(planService.createPlan(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubscriptionPlanDto> update(
            @PathVariable String id,
            @RequestBody SubscriptionPlanDto dto) throws BadRequestException {
        return ResponseEntity.ok(planService.updatePlan(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) throws BadRequestException {
        planService.deletePlan(id);
        return ResponseEntity.noContent().build();
    }
}

