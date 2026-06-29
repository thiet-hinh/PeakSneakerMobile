package com.example.shoestore.service;

import com.example.shoestore.entity.Image;
import com.example.shoestore.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public Image findById(Integer imageId) {
        return imageRepository.findById(imageId).orElseThrow(() -> new RuntimeException("Image not found: " + imageId));
    }

    public List<Image> findByProductVariantId(Integer variantId) {
        return imageRepository.findByProductVariantId(variantId);
    }

    public Image findPrimaryByProductId(Integer productId) {
        return imageRepository.findByProductIdAndIsPrimaryTrue(productId)
                .orElseThrow(() -> new RuntimeException("No primary image for product: " + productId));
    }

    @Transactional
    public Image save(Image image) {
        return imageRepository.save(image);
    }

    @Transactional
    public List<Image> saveAll(List<Image> images) {
        return imageRepository.saveAll(images);
    }

    @Transactional
    public void setPrimary(Integer imageId, Integer productId) {
        imageRepository.findByProductIdAndIsPrimaryTrue(productId).ifPresent(img -> {
            img.setIsPrimary(false);
            imageRepository.save(img);
        });
        Image image = findById(imageId);
        image.setIsPrimary(true);
        imageRepository.save(image);
    }


}
