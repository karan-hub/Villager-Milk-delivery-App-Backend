package com.karan.village_milk_app.Service.Impl;

import com.karan.village_milk_app.Dto.AddressDTO;
import com.karan.village_milk_app.Exceptions.ResourceNotFoundException;
import com.karan.village_milk_app.Repositories.AddressRepository;
import com.karan.village_milk_app.Repositories.UserRepository;
import com.karan.village_milk_app.Request.AddressRequest;
import com.karan.village_milk_app.Response.AddressResponse;
import com.karan.village_milk_app.Service.AddressService;
import com.karan.village_milk_app.healpers.UserHelper;
import com.karan.village_milk_app.model.Address;
import com.karan.village_milk_app.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    private User resolveUser(UUID userId) {
        if (userId != null) {
            return userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        }
        return UserHelper.getCurrentUser(userRepository);
    }

    @Override
    @Transactional
    public AddressResponse create(AddressRequest request) {
        User user = resolveUser(request.getUserId());

        Address address = Address.builder()
                .flatNumber(request.getFlatNumber())
                .buildingName(request.getBuildingName())
                .area(request.getArea())
                .landmark(request.getLandmark())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .country("India")
                .isDefault(false)
                .user(user)
                .build();
        user.addAddress(address);
        return toResponse(addressRepository.save(address));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getMyAddresses() {
        UUID userId = UserHelper.getCurrentUser(userRepository).getId();
        return addressRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse getDefault() {
        UUID userId = UserHelper.getCurrentUser(userRepository).getId();
        Address address = addressRepository.findByUserIdAndIsDefaultTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Default address not found"));
        return toResponse(address);
    }

    @Override
    public AddressResponse update(UUID addressId, AddressRequest request) {
        Address address = findOwnedAddress(addressId);

        address.setFlatNumber(request.getFlatNumber());
        address.setBuildingName(request.getBuildingName());
        address.setArea(request.getArea());
        address.setLandmark(request.getLandmark());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setLatitude(request.getLatitude());
        address.setLongitude(request.getLongitude());

        return toResponse(address);
    }

    @Override
    public void delete(UUID addressId) {
        addressRepository.delete(findOwnedAddress(addressId));
    }

    @Override
    public AddressResponse markDefault(UUID addressId) {
        Address address = findOwnedAddress(addressId);
        UUID userId = address.getUser().getId();

        addressRepository.clearDefaultAddress(userId);
        address.setDefault(true);

        return toResponse(address);
    }

    private Address findOwnedAddress(UUID addressId) {
        User user = UserHelper.getCurrentUser(userRepository);
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Unauthorized address access");
        }
        return address;
    }

    private AddressResponse toResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .flatNumber(address.getFlatNumber())
                .buildingName(address.getBuildingName())
                .area(address.getArea())
                .landmark(address.getLandmark())
                .city(address.getCity())
                .state(address.getState())
                .pincode(address.getPincode())
                .country(address.getCountry())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .isDefault(address.isDefault())
                .build();
    }
}
