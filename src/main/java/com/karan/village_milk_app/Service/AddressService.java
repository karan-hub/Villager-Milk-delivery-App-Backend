package com.karan.village_milk_app.Service;

import com.karan.village_milk_app.Request.AddressRequest;
import com.karan.village_milk_app.Response.AddressResponse;
import com.karan.village_milk_app.model.Address;

import java.util.List;
import java.util.UUID;

public interface AddressService {

    AddressResponse create(AddressRequest request);

    List<AddressResponse> getMyAddresses();

    AddressResponse getDefault();

    AddressResponse update(UUID addressId, AddressRequest request);

    void delete(UUID addressId);

    AddressResponse markDefault(UUID addressId);
}
