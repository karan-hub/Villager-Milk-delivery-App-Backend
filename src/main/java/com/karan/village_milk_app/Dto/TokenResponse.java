package com.karan.village_milk_app.Dto;

import com.karan.village_milk_app.model.User;

public record TokenResponse(
        String accessToken ,
        String refreshToken ,
        long expire,
        String tokenType,
        UserDTO  user
) {
    public  static  TokenResponse of(String  accessToken , String  refreshToken , long expire , UserDTO user){
        return  new TokenResponse(accessToken , refreshToken , expire,"Bearer" ,user);
    }
}
