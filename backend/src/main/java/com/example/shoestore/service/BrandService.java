package com.example.shoestore.service;


import com.example.shoestore.dto.response.BrandResponse;
import com.example.shoestore.entity.Brand;
import com.example.shoestore.entity.Image;
import com.example.shoestore.repository.BrandRepository;
import com.example.shoestore.repository.ImageRepository;
import com.example.shoestore.thirdparty.cloudinary.CloudinaryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;
    private final ImageRepository imageRepository;
    private final CloudinaryService cloudinaryService;

    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    public Brand findById(Integer id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found with id: " + id));
    }
    public List<BrandResponse> findAllWithImage() {
        return brandRepository.findAll().stream()
                .map(this::toBrandResponse)
                .collect(Collectors.toList());
    }

    private BrandResponse toBrandResponse(Brand brand) {
        String imageUrl = resolveBrandImage(brand);
        return BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .description(brand.getDescription())
                .imageUrl(imageUrl)
                .build();
    }

    private String resolveBrandImage(Brand brand) {
        if (brand.getImageName() != null && !brand.getImageName().isBlank()) {
            return cloudinaryService.createImageUrl(brand.getImageName());
        }

        Optional<Image> image = imageRepository.findFirstByProduct_Brand_IdAndIsPrimaryTrueOrderByIdAsc(brand.getId());
        if (image.isEmpty()) {
            image = imageRepository.findFirstByProduct_Brand_IdOrderByIdAsc(brand.getId());
        }

        return image.map(img -> cloudinaryService.createImageUrl(img.getImageName())).orElse(null);
    }
}
