package com.karan.village_milk_app.Service;

import com.karan.village_milk_app.Dto.SubscriptionPlanDto;

import java.util.List;

public interface SubscriptionPlanService {

    List<SubscriptionPlanDto> getAllPlans();

    SubscriptionPlanDto getPlanById(String id);

    SubscriptionPlanDto createPlan(SubscriptionPlanDto dto);

    SubscriptionPlanDto updatePlan(String id, SubscriptionPlanDto dto);

    void deletePlan(String id);
}
