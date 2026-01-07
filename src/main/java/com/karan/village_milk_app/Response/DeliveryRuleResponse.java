package com.karan.village_milk_app.Response;

import lombok.Data;

import java.time.DayOfWeek;

@Data
public class DeliveryRuleResponse {
    private DayOfWeek dayOfWeek;
    private Integer units;
}
