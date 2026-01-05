package com.karan.village_milk_app.Controller;

import com.karan.village_milk_app.Request.AddressRequest;
import com.karan.village_milk_app.Response.AddressResponse;
import com.karan.village_milk_app.Service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
@Validated
public class AddressController {

    private final AddressService addressService;


    @PostMapping
    public ResponseEntity<AddressResponse> createAddress(
            @Valid @RequestBody AddressRequest request
    ) {
        AddressResponse response = addressService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping
    public ResponseEntity<List<AddressResponse>> getMyAddresses() {
        return ResponseEntity.ok(addressService.getMyAddresses());
    }


    @GetMapping("/default")
    public ResponseEntity<AddressResponse> getDefaultAddress() {
        return ResponseEntity.ok(addressService.getDefault());
    }


    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(
            @PathVariable UUID addressId,
            @Valid @RequestBody AddressRequest request
    ) {
        return ResponseEntity.ok(
                addressService.update(addressId, request)
        );
    }


    @PutMapping("/{addressId}/default")
    public ResponseEntity<AddressResponse> markDefaultAddress(
            @PathVariable UUID addressId
    ) {
        return ResponseEntity.ok(
                addressService.markDefault(addressId)
        );
    }


    @DeleteMapping("/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAddress(@PathVariable UUID addressId) {
        addressService.delete(addressId);
    }
}
