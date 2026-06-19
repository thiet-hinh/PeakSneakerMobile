package com.example.shoestore.service;


import com.example.shoestore.entity.Brand;
import com.example.shoestore.repository.BrandRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    public Brand findById(Integer id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found with id: " + id));
    }

    @Transactional
    public Brand save(Brand brand) {
        if (brandRepository.existsByName(brand.getName())) {
            throw new RuntimeException("Brand name already exists: " + brand.getName());
        }
        return brandRepository.save(brand);
    }

    @Transactional
    public Brand update(Integer id, Brand updated) {
        Brand existing = findById(id);
        existing.setName(updated.getName());
        existing.setImageName(updated.getImageName());
        existing.setDescription(updated.getDescription());
        return brandRepository.save(existing);
    }
}
