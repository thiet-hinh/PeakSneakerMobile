package com.example.shoestore.controller;

import com.example.shoestore.dto.request.AddressRequest;
import com.example.shoestore.dto.response.AddressItemResponse;
import com.example.shoestore.dto.response.AddressResponse;
import com.example.shoestore.entity.Address;
import com.example.shoestore.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/user/{userId}/address/default")
    public ResponseEntity<AddressResponse> getDefault(
            @PathVariable Integer userId) {

        return ResponseEntity.ok(addressService.findDefaultByUserId(userId));
    }

    @PostMapping("/user/{userId}/address/default")
    public ResponseEntity<AddressResponse> updateDefault(
            @PathVariable Integer userId,
            @RequestBody AddressRequest address) {

        return ResponseEntity.ok(addressService.saveOrUpdate(userId, address));
    }


    @GetMapping("/address/ghn/province")
    public ResponseEntity<List<AddressItemResponse>> getProvinces() {
        return ResponseEntity.ok(addressService.getProvinces());
    }

    @GetMapping("/address/ghn/district/{provinceId}")
    public ResponseEntity<List<AddressItemResponse>> getDistricts(
            @PathVariable Integer provinceId) {

        return ResponseEntity.ok(addressService.getDistricts(provinceId));
    }

    @GetMapping("/address/ghn/ward/{districtId}")
    public ResponseEntity<List<AddressItemResponse>> getWards(
            @PathVariable Integer districtId) {

        return ResponseEntity.ok(addressService.getWards(districtId));
    }

    @PostMapping
    public ResponseEntity<Address> create(@PathVariable Integer userId, @RequestBody Address address) {
        return ResponseEntity.ok(addressService.save(address));
    }
}
