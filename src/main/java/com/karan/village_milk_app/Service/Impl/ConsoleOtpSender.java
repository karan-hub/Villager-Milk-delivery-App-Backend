package com.karan.village_milk_app.Service.Impl;

import com.karan.village_milk_app.Service.OtpSender;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class ConsoleOtpSender implements OtpSender {

    @Override
    public void send(String phone, String otp) {
        System.out.println("OTP sent to " + phone + ": " + otp);
    }
}
