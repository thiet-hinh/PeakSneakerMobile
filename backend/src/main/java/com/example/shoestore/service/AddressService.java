package com.example.shoestore.service;

import com.example.shoestore.entity.Address;
import com.example.shoestore.repository.AddressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public List<Address> findByUserId(Integer userId) {
        return addressRepository.findByUserId(userId);
    }


    public Address findDefaultByUserId(Integer userId) {
        return addressRepository.findByUserIdAndIsDefaultTrue(userId)
                .orElseThrow(() -> new RuntimeException("No default address for user: " + userId));
    }

    @Transactional
    public Address save(Address address) {
        return addressRepository.save(address);
    }

    @Transactional
    public Address update(Integer userId, Address updated) {
        Address existing = findDefaultByUserId(userId);
        existing.setUserName(updated.getUserName());
        existing.setPhone(updated.getPhone());
        existing.setProvinceId(updated.getProvinceId());
        existing.setProvinceName(updated.getProvinceName());
        existing.setDistrictId(updated.getDistrictId());
        existing.setDistrictName(updated.getDistrictName());
        existing.setWardId(updated.getWardId());
        existing.setWardName(updated.getWardName());
        existing.setStreetDetail(updated.getStreetDetail());
        existing.setIsDefault(true);
        return addressRepository.save(existing);
    }

}
