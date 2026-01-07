package com.karan.village_milk_app.Service.Impl;

import com.karan.village_milk_app.Request.SignupRequest;
import com.karan.village_milk_app.Dto.UserDTO;
import com.karan.village_milk_app.Repositories.UserRepository;
import com.karan.village_milk_app.Exceptions.ResourceNotFoundException;
import com.karan.village_milk_app.Service.UserService;
import com.karan.village_milk_app.healpers.UserHelper;
import com.karan.village_milk_app.model.Type.Role;
import com.karan.village_milk_app.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public UserDTO createUser(SignupRequest signupRequest) {
        if(signupRequest.getPhone() == null || signupRequest.getPhone().isBlank() || userRepository.existsByPhone(signupRequest.getPhone())) {
            throw new IllegalArgumentException("Invalid or duplicate phone");
        }
        if(signupRequest.getPassword() == null || signupRequest.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        User user = new User();
        user.setName(signupRequest.getName());
        user.setPhone(signupRequest.getPhone());
        user.setPassword(encodedPassword);
        user.setRole(Role.ROLE_USER);
        user.setEnabled(true);
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        User saveUser = userRepository.save(user);
        return modelMapper.map(saveUser , UserDTO.class);
    }

    @Override
    public UserDTO createUser(UserDTO dto) {
        if(dto.getPhone() == null || dto.getPhone().isBlank() || userRepository.existsByPhone(dto.getPhone())) {
            throw new IllegalArgumentException("Invalid or duplicate phone");
        }
        if(dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        dto.setRole(Role.ROLE_USER);
        dto.setEnabled(true);
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
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO updateUser(UserDTO dto, String id) throws BadRequestException {
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
        if (dto.getEnabled() != null) {
            existingUser.setEnabled(dto.getEnabled());
        }


//        existingUser.setEnable(dto.isEnable());
        existingUser.setUpdatedAt(Instant.now());
        User save = userRepository.save(existingUser);
        return  modelMapper.map(save , UserDTO.class);
    }

    @Override
    public void deleteUser(String userId) throws BadRequestException {
        UUID uuid = UserHelper.parseUUID(userId);
        User  user = userRepository.findById(uuid).orElseThrow(()-> new ResourceNotFoundException("NO USER FOUND FOR THIS NUMBER"));
        userRepository.delete(user);
    }

    @Override
    public UserDTO getUserById(String userId) throws BadRequestException {
        UUID uuid = UserHelper.parseUUID(userId);
        User  user = userRepository.findById(uuid).orElseThrow(()-> new ResourceNotFoundException("NO USER FOUND FOR THIS MAIL"));

        return  modelMapper.map(user , UserDTO.class);
    }

    @Override
    @Transactional
    public Page<UserDTO> getAllUsers(int page , int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository
                .findAll(pageable)
                .map((user) -> {
                    return modelMapper.map(user, UserDTO.class);
                });
    }

    @Override
    public UserDTO createUserViaOtp(String phone) {

        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone is required");
        }

        if (userRepository.existsByPhone(phone)) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = new User();
        user.setPhone(phone);
        user.setName("New User");
        user.setRole(Role.ROLE_USER);
        user.setEnabled(true);
        user.setPassword(null); // OTP-only user
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        return modelMapper.map(userRepository.save(user), UserDTO.class);
    }

    @Override
    public void makeAdmin(String userId) throws BadRequestException {
        UUID uuid = UserHelper.parseUUID(userId);
        User user = userRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getRole() == Role.ROLE_ADMIN) {
            throw new IllegalArgumentException("User is already an admin");
        }
        user.setRole(Role.ROLE_ADMIN);
        userRepository.save(user);
    }

}
