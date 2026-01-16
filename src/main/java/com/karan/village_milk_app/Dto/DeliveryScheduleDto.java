package com.karan.village_milk_app.Dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class DeliveryScheduleDto {

    @Min(0) @Max(6)
    private int dayOfWeek;

    @Min(1)
    private int units;
}
