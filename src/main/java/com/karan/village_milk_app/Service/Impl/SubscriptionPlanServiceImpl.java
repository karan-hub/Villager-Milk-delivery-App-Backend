package com.karan.village_milk_app.Service.Impl;

import com.karan.village_milk_app.Dto.SubscriptionPlanDto;
import com.karan.village_milk_app.Repositories.*;
import com.karan.village_milk_app.Service.SubscriptionPlanService;
import com.karan.village_milk_app.healpers.UserHelper;
import com.karan.village_milk_app.model.*;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository planRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionPlanDto> getAllPlans() {

        return planRepository.findAll()
                .stream()
                .map(plan -> modelMapper.map(plan, SubscriptionPlanDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SubscriptionPlanDto getPlanById(String id) throws BadRequestException {

        UUID planId = UserHelper.parseUUID(id);

        SubscriptionPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Subscription plan not found"));

        return modelMapper.map(plan, SubscriptionPlanDto.class);
    }

    @Override
    public List<SubscriptionPlanDto> getPlansByProductId(UUID productId) {
        return planRepository.findAllByProduct_Id(productId)
                .stream()
                .map(plan -> modelMapper.map(plan, SubscriptionPlanDto.class))
                .toList();
    }



    @Override
    public SubscriptionPlanDto createPlan(SubscriptionPlanDto dto) {

        SubscriptionPlan plan = new SubscriptionPlan();

        plan.setPlanKey(dto.getPlanKey());
        plan.setTitle(dto.getTitle());
        plan.setDurationDays(dto.getDurationDays());
        plan.setUnits(dto.getUnits());
        plan.setPrice(dto.getPrice());
        plan.setOffer(dto.getOffer());

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        plan.setProduct(product);

        SubscriptionPlan saved = planRepository.save(plan);
        return modelMapper.map(saved, SubscriptionPlanDto.class);
    }

    @Override
    public SubscriptionPlanDto updatePlan(String id, SubscriptionPlanDto dto) throws BadRequestException {

        UUID planId = UserHelper.parseUUID(id);

        SubscriptionPlan existing = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Subscription plan not found"));

        existing.setPlanKey(dto.getPlanKey());
        existing.setTitle(dto.getTitle());
        existing.setDurationDays(dto.getDurationDays());
        existing.setUnits(dto.getUnits());
        existing.setPrice(dto.getPrice());
        existing.setOffer(dto.getOffer());

        if (dto.getProductId() != null) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            existing.setProduct(product);
        }

        SubscriptionPlan saved = planRepository.save(existing);
        return modelMapper.map(saved, SubscriptionPlanDto.class);
    }

    @Override
    public void deletePlan(String id) throws BadRequestException {

        UUID planId = UserHelper.parseUUID(id);
        planRepository.deleteById(planId);
    }
}
