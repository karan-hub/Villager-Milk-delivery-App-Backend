package com.karan.village_milk_app.Service;

import com.karan.village_milk_app.Dto.SubscriptionPlanDto;
import org.apache.coyote.BadRequestException;

import java.util.List;
import java.util.UUID;

public interface SubscriptionPlanService {

    List<SubscriptionPlanDto> getAllPlans();

    SubscriptionPlanDto getPlanById(String id) throws BadRequestException;
    List<SubscriptionPlanDto> getPlansByProductId(UUID id);

    SubscriptionPlanDto createPlan(SubscriptionPlanDto dto);

    SubscriptionPlanDto updatePlan(String id, SubscriptionPlanDto dto) throws BadRequestException;

    void deletePlan(String id) throws BadRequestException;
}
