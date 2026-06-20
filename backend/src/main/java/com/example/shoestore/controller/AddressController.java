package com.example.shoestore.controller;

import com.example.shoestore.entity.Address;
import com.example.shoestore.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/default")
    public ResponseEntity<Address> getDefault(@PathVariable Integer userId) {
        return ResponseEntity.ok(addressService.findDefaultByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Address> create(@PathVariable Integer userId, @RequestBody Address address) {
        return ResponseEntity.ok(addressService.save(address));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> update(@PathVariable Integer id, @RequestBody Address address) {
        return ResponseEntity.ok(addressService.update(id, address));
    }
}
