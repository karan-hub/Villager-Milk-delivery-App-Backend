package com.karan.village_milk_app.Service;

import com.karan.village_milk_app.Dto.UserDTO;
import com.karan.village_milk_app.model.User;

public interface OtpService {
     public  void requestOtp(String phone);
    public UserDTO verifyOtp(String phone , String otp);
}
