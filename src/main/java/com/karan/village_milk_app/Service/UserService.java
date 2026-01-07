package com.karan.village_milk_app.Service;

import com.karan.village_milk_app.Request.SignupRequest;
import com.karan.village_milk_app.Dto.UserDTO;
import org.apache.coyote.BadRequestException;

public interface UserService {
    UserDTO createUser(SignupRequest  signupRequest);
    UserDTO createUser(UserDTO  userDto);
    UserDTO getUserByPhone(String  phone);
    UserDTO updateUser(UserDTO  userDto , String  userId) throws BadRequestException;
    void deleteUser(String userId) throws BadRequestException;
    UserDTO  getUserById(String userId) throws BadRequestException;
    public UserDTO createUserViaOtp(String phone);
    Iterable<UserDTO> getAllUsers(int page , int size);
    void makeAdmin(String userId) throws BadRequestException;
}
