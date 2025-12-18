package com.karan.village_milk_app.Service;

import com.karan.village_milk_app.Dto.UserDTO;

public interface UserService {
    UserDTO createUser(UserDTO  userDto);
    UserDTO getUserByPhone(String  phone);
    UserDTO updateUser(UserDTO  userDto , String  userId);
    void deleteUser(String userId);
    UserDTO  getUserById(String userId);
    public UserDTO createUserViaOtp(String phone);
    Iterable<UserDTO> getAllUsers(int page , int size);
}
