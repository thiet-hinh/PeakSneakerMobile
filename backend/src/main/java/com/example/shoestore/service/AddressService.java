package com.example.shoestore.service;

import com.example.shoestore.dto.request.AddressRequest;
import com.example.shoestore.dto.response.AddressItemResponse;
import com.example.shoestore.dto.response.AddressResponse;
import com.example.shoestore.dto.response.UserResponse;
import com.example.shoestore.entity.Address;
import com.example.shoestore.entity.User;
import com.example.shoestore.repository.AddressRepository;
import com.example.shoestore.repository.UserRepository;
import com.example.shoestore.thirdparty.ghn.GHNService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final GHNService ghnService;


    public AddressResponse findDefaultByUserId(Integer userId) {

        Address address = addressRepository.findByUserIdAndIsDefaultTrue(userId)
                .orElseThrow(() -> new RuntimeException("No default address"));

        return AddressResponse.builder()
                .id(address.getId())
                .provinceId(address.getProvinceId())
                .provinceName(address.getProvinceName())
                .districtId(address.getDistrictId())
                .districtName(address.getDistrictName())
                .wardId(address.getWardId())
                .wardName(address.getWardName())
                .streetDetail(address.getStreetDetail())
                .userName(address.getUserName())
                .phone(address.getPhone())
                .build();
    }

    @Transactional
    public Address save(Address address) {
        return addressRepository.save(address);
    }

    @Transactional
    public AddressResponse saveOrUpdate(Integer userId, AddressRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address address = addressRepository.findByUserIdAndIsDefaultTrue(userId)
                .orElse(new Address());

        address.setUser(user);
        address.setProvinceId(request.getProvinceId());
        address.setProvinceName(request.getProvinceName());
        address.setDistrictId(request.getDistrictId());
        address.setDistrictName(request.getDistrictName());
        address.setWardId(request.getWardId());
        address.setWardName(request.getWardName());
        address.setStreetDetail(request.getStreetDetail());
        address.setUserName(request.getUserName());
        address.setPhone(request.getPhone());
        address.setIsDefault(true);

        address = addressRepository.save(address);
        return AddressResponse.builder()
                .id(address.getId())
                .provinceId(address.getProvinceId())
                .provinceName(address.getProvinceName())
                .districtId(address.getDistrictId())
                .districtName(address.getDistrictName())
                .wardId(address.getWardId())
                .wardName(address.getWardName())
                .streetDetail(address.getStreetDetail())
                .userName(address.getUserName())
                .phone(address.getPhone())
                .build();
    }

    public List<AddressItemResponse> getProvinces() {
        return ghnService.getProvinceList();
    }

    public List<AddressItemResponse> getDistricts(Integer provinceId) {
        return ghnService.getDistrictList(provinceId);
    }

    public List<AddressItemResponse> getWards(Integer districtId) {
        return ghnService.getWardList(districtId);
    }

}
