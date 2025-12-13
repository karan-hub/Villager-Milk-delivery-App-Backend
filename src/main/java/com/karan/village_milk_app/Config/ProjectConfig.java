package com.karan.village_milk_app.Config;

import com.karan.village_milk_app.Dto.UserDTO;
import com.karan.village_milk_app.healpers.OtpGenerator;
import com.karan.village_milk_app.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ProjectConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();


        mapper.typeMap(UserDTO.class, User.class).addMappings(m -> {
            m.skip(User::setPassword);
        });

        mapper.typeMap(User.class, UserDTO.class).addMappings(m -> {
            m.skip(UserDTO::setPassword);
        });

        return mapper;
    }


//       OTP Generator

    @Bean
    public OtpGenerator otpGenerator() {
        return new OtpGenerator();
    }


//       Time

    @Bean
    public Clock systemClock() {
        return Clock.systemUTC();
    }


}
