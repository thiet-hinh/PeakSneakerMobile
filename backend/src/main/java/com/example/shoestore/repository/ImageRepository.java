package com.example.shoestore.repository;

import com.example.shoestore.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Integer> {

    List<Image> findByProductId(Integer productId);

    List<Image> findByProductVariantId(Integer productVariantId);

    Optional<Image> findByProductIdAndIsPrimaryTrue(Integer productId);

    Optional<Image> findByImageName(String imageName);

    Optional<Image> findFirstByProductIdAndIsPrimaryTrue(Integer productId);
}
