package com.karan.village_milk_app.Service.Impl;

import com.karan.village_milk_app.Dto.UserDTO;
import com.karan.village_milk_app.Repositories.UserRepository;
import com.karan.village_milk_app.Exceptions.ResourceNotFoundException;
import com.karan.village_milk_app.Service.UserService;
import com.karan.village_milk_app.healpers.UserHelper;
import com.karan.village_milk_app.model.Type.Role;
import com.karan.village_milk_app.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private  final UserRepository userRepository ;
    private  final ModelMapper modelMapper;
    private  final PasswordEncoder passwordEncoder;
    @Override
    public UserDTO createUser(UserDTO dto) {
        if(dto.getPhone() == null || dto.getPhone().isBlank() || userRepository.existsByPhone(dto.getPhone())) {
            throw new IllegalArgumentException("Invalid or duplicate phone");
        }
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        dto.setRole(Role.ROLE_USER);
        dto.setEnable(true);
        dto.setCreatedAt(Instant.now());
        dto.setUpdatedAt(Instant.now());

        User user = modelMapper.map(dto , User.class);
        user.setPassword(encodedPassword);
        User saveUser = userRepository.save(user);
         return modelMapper.map(saveUser , UserDTO.class);
    }

    @Override
    public UserDTO getUserByPhone(String phone) {
        User  user = userRepository.findByPhone(phone).orElseThrow(()->new ResourceNotFoundException("Number IS NOT FOUND"));
        user.setPassword(null);
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO updateUser(UserDTO dto, String id) {
        UUID uuid = UserHelper.parseUUID(id);
        User existingUser = userRepository.findById(uuid).orElseThrow(()-> new ResourceNotFoundException("NO USER FOUND FOR THIS Number"));

        if(dto.getName() != null) existingUser.setName(dto.getName() );


        if(dto.getPhone() != null && !dto.getPhone().isBlank()) {
            if(userRepository.existsByPhone(dto.getPhone())
                    && ! existingUser.getPhone().equals(dto.getPhone())) {
                throw new IllegalArgumentException("Phone already in use");
            }
            existingUser.setPhone(dto.getPhone());
        }

        if(dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        existingUser.setEnable(dto.isEnable());
        existingUser.setUpdatedAt(Instant.now());
        User save = userRepository.save(existingUser);
        return  modelMapper.map(save , UserDTO.class);
    }

    @Override
    public void deleteUser(String userId) {
        UUID uuid = UserHelper.parseUUID(userId);
        User  user = userRepository.findById(uuid).orElseThrow(()-> new ResourceNotFoundException("NO USER FOUND FOR THIS NUMBER"));
        userRepository.delete(user);
    }

    @Override
    public UserDTO getUserById(String userId) {
        UUID uuid = UserHelper.parseUUID(userId);
        User  user = userRepository.findById(uuid).orElseThrow(()-> new ResourceNotFoundException("NO USER FOUND FOR THIS MAIL"));
        user.setPassword(null);
        return  modelMapper.map(user , UserDTO.class);
    }

    @Override
    @Transactional
    public Iterable<UserDTO> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map((user) -> {
                    user.setPassword(null);
                    return modelMapper.map(user, UserDTO.class);
                })
                .toList();
    }
}
